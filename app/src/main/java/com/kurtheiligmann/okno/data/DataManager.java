package com.kurtheiligmann.okno.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kurtheiligmann.okno.R;
import com.kurtheiligmann.okno.media.MediaManager;

import java.util.ArrayList;
import java.util.List;

public class DataManager extends SQLiteOpenHelper {
    public static final String PREFS_NAME = "com.kurtheiligmann.okno.data.prefs";

    private static final String DATABASE_NAME = "okno.db";
    private static final int DATABASE_VERSION = 1;

    private static final String ENABLED_KEY = "com.kurtheiligmann.okno.data.enabledKey";
    private static final String FIRST_RUN_KEY = "com.kurtheiligmann.okno.data.firstRunKey";

    private static final String CREATE_MESSAGE_TABLE =
            "CREATE TABLE " + Message.TABLE_NAME + "(" +
                    Message.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Message.COLUMN_BODY + " TEXT NOT NULL UNIQUE," +
                    Message.COLUMN_RINGTONE_NAME + " TEXT NOT NULL" +
                    ");";

    private Context context;
    private SQLiteDatabase database;

    public DataManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setContext(context);
        if (isFirstRun()) {
            doInitialSetup();
        }
        setDatabase(getWritableDatabase());
    }

    private void doInitialSetup() {
        MediaManager mediaManager = new MediaManager(getContext());
        mediaManager.createOkNoRingtones();
        isFirstRun(false);
    }

    public void close() {
        if (getDatabase() != null) {
            getDatabase().close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i(this.getClass().getName(), CREATE_MESSAGE_TABLE);
        database.execSQL(CREATE_MESSAGE_TABLE);
        setDatabase(database);
        initDefaultSounds(getContext());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(this.getClass().getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Message.TABLE_NAME);
        onCreate(db);
    }

    public void saveEnabled(boolean enabled) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ENABLED_KEY, enabled);
        editor.apply();
    }

    public boolean getEnabled() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(ENABLED_KEY, true);
    }

    private void isFirstRun(boolean isFirstRun) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_RUN_KEY, isFirstRun);
        editor.apply();
    }

    private boolean isFirstRun() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(FIRST_RUN_KEY, true);
    }

    private void initDefaultSounds(Context context) {
        String[] defaultPositiveValues = context.getResources().getStringArray(R.array.default_positive_values);
        for (String defaultPositiveValue : defaultPositiveValues) {
            Message message = Message.getInstance(defaultPositiveValue, MediaManager.DEFAULT_POSITIVE_TITLE);
            saveMessage(message);
        }

        String[] defaultNegativeValues = context.getResources().getStringArray(R.array.default_negative_values);
        for (String defaultNegativeValue : defaultNegativeValues) {
            Message message = Message.getInstance(defaultNegativeValue, MediaManager.DEFAULT_NEGATIVE_TITLE);
            saveMessage(message);
        }
    }

    public List<Message> getAllMessages() throws SQLiteException {
        List<Message> existingMessages = new ArrayList<>();
        Cursor cursor = getDatabase().query(Message.TABLE_NAME, Message.ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = Message.getInstance(cursor);
            existingMessages.add(message);
            cursor.moveToNext();
        }
        cursor.close();

        return existingMessages;
    }


    public Message getMessage(long messageId) {
        Message message = null;
        try {
            Cursor cursor = getDatabase().query(Message.TABLE_NAME, Message.ALL_COLUMNS, Message.COLUMN_ID + "=" + messageId, null, null, null, null);
            cursor.moveToFirst();
            message = Message.getInstance(cursor);
            cursor.close();
        } catch (SQLiteException e) {
            Log.e(this.getClass().getName(), e.getLocalizedMessage());
        }

        return message;
    }

    public void saveMessage(Message message) {
        ContentValues values = new ContentValues();
        values.put(Message.COLUMN_BODY, message.getBody());
        values.put(Message.COLUMN_RINGTONE_NAME, message.getRingtoneName());

        if (message.getId() == -1 || getMessage(message.getId()) == null) {
            //save message
            message.setId(getDatabase().insert(Message.TABLE_NAME, null, values));
        } else {
            //update message
            getDatabase().update(Message.TABLE_NAME, values, Message.COLUMN_ID + "=?", new String[]{message.getId() + ""});
        }
    }

    public Message getMessageWithBody(String body) {
        Message message = null;

        Cursor cursor = getDatabase().query(Message.TABLE_NAME, Message.ALL_COLUMNS, Message.COLUMN_BODY + "=?", new String[]{body}, null, null, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            message = Message.getInstance(cursor);
        }
        cursor.close();
        return message;
    }

    public void deleteMessage(Message message) {
        getDatabase().delete(Message.TABLE_NAME, Message.COLUMN_ID + "=?", new String[]{message.getId() + ""});
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private SQLiteDatabase getDatabase() {
        return database;
    }

    private void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
