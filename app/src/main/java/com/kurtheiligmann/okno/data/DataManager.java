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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kurtheiligmann on 2/9/15.
 */
public class DataManager extends SQLiteOpenHelper {
    public static final String PREFS_NAME = "com.kurtheiligmann.okno.data.prefs";

    private static final String DATABASE_NAME = "okno.db";
    private static final int DATABASE_VERSION = 1;

    private static final String ENABLED_KEY = "com.kurtheiligmann.okno.data.enabledKey";
    private static final String DEFAULT_POSITIVE_SOUND = "android.resource://com.kurtheiligmann.okno/raw/two_tone";
    private static final String DEFAULT_NEGATIVE_SOUND = "android.resource://com.kurtheiligmann.okno/raw/no_tone";

    private static final String CREATE_TONE_TABLE =
            "CREATE TABLE " + Tone.TABLE_NAME + "(" +
                    Tone.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Tone.COLUMN_TITLE + " TEXT NOT NULL," +
                    Tone.COLUMN_FILE_ADDRESS + " TEXT NOT NULL" +
                    ");";
    private static final String CREATE_MESSAGE_TABLE =
            "CREATE TABLE " + Message.TABLE_NAME + "(" +
                    Message.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Message.COLUMN_BODY + " TEXT NOT NULL," +
                    Message.COLUMN_TONE_ID + " INTEGER," +
                    "FOREIGN KEY(" + Message.COLUMN_TONE_ID + ") REFERENCES " + Tone.TABLE_NAME + "(" + Tone.COLUMN_ID + ")" +
                    ");";

    private Context context;
    private SQLiteDatabase database;

    public DataManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setContext(context);
        setDatabase(getWritableDatabase());
    }

    public void close() {
        database.close();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i(this.getClass().getName(), CREATE_TONE_TABLE);
        database.execSQL(CREATE_TONE_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS " + Tone.TABLE_NAME);
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

    private void initDefaultSounds(Context context) {
        String[] defaultPositiveValues = context.getResources().getStringArray(R.array.default_positive_values);
        for (String defaultPositiveValue : defaultPositiveValues) {
            Tone tone = new Tone("OkYes", DEFAULT_POSITIVE_SOUND);
            Message message = new Message(defaultPositiveValue, tone);
            saveMessage(message);
        }

        String[] defaultNegativeValues = context.getResources().getStringArray(R.array.default_negative_values);
        for (String defaultNegativeValue : defaultNegativeValues) {
            Tone tone = new Tone("OkNo", DEFAULT_NEGATIVE_SOUND);
            Message message = new Message(defaultNegativeValue, tone);
            saveMessage(message);
        }
    }

    public List<Message> getAllMessages() throws SQLiteException {
        List<Message> existingMessages = new ArrayList<>();
        Cursor cursor = getDatabase().query(Message.TABLE_NAME, Message.ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = messageFromCursor(cursor);
            existingMessages.add(message);
            cursor.moveToNext();
        }

        return existingMessages;
    }

    public Tone getTone(long toneId) {
        Tone tone = null;
        try {
            Cursor cursor = getDatabase().query(Tone.TABLE_NAME, Tone.ALL_COLUMNS, Tone.COLUMN_ID + "=" + toneId, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String title = cursor.getString(1);
                String fileAddress = cursor.getString(2);
                tone = new Tone(toneId, title, fileAddress);
                cursor.moveToNext();
            }
        } catch (SQLiteException e) {
            Log.e(this.getClass().getName(), e.getLocalizedMessage());
        }
        return tone;
    }

    public Message getMessage(long messageId) {
        Message message = null;
        try {
            Cursor cursor = getDatabase().query(Message.TABLE_NAME, Message.ALL_COLUMNS, Message.COLUMN_ID + "=" + messageId, null, null, null, null);
            cursor.moveToFirst();
            message = messageFromCursor(cursor);
        } catch (SQLiteException e) {
            Log.e(this.getClass().getName(), e.getLocalizedMessage());
        }

        return message;
    }

    private Message messageFromCursor(Cursor cursor) {
        long messageId = cursor.getLong(0);
        String body = cursor.getString(1);
        Tone tone = getTone(cursor.getInt(2));
        Message message = new Message(messageId, body, tone);

        return message;
    }

    public void saveMessage(Message message) {
        saveTone(message.getTone());
        if (message.getId() == -1 || getMessage(message.getId()) == null) {
            //save message
            ContentValues values = new ContentValues();
            values.put(Message.COLUMN_BODY, message.getBody());
            values.put(Message.COLUMN_TONE_ID, message.getTone().getId());

            message.setId(getDatabase().insert(Message.TABLE_NAME, null, values));
        } else {
//            update message
        }
    }

    private void saveTone(Tone tone) {
        if (tone.getId() == -1 || getTone(tone.getId()) == null) {
            ContentValues toneValues = new ContentValues();
            toneValues.put(Tone.COLUMN_TITLE, tone.getTitle());
            toneValues.put(Tone.COLUMN_FILE_ADDRESS, tone.getFileAddress());

            tone.setId(getDatabase().insert(Tone.TABLE_NAME, null, toneValues));
        } else {
            //update tone
        }
    }

    public Message getMessageWithBody(String body) {
        Message message = null;
//        List<Message> foundMessages = Message.find(Message.class, "body = ?", body.toLowerCase());
//        if (foundMessages.size() > 0) {
//            message = foundMessages.get(0);
//        }

        return message;
    }

    public void deleteMessage(Message message) {
        getDatabase().delete(Message.TABLE_NAME, Message.COLUMN_ID + "=?", new String[]{message.getId() + ""});
        deleteToneIfUnused(message.getTone());
    }

    public void deleteTone(Tone tone) {
        getDatabase().delete(Tone.TABLE_NAME, Tone.COLUMN_ID + "=?", new String[]{tone.getId() + ""});
    }

    private void deleteToneIfUnused(Tone tone) {
        Cursor cursor = getDatabase().query(Message.TABLE_NAME, new String[]{Message.COLUMN_ID}, Message.COLUMN_TONE_ID + "=?", new String[]{tone.getId() + ""}, null, null, null);
        if (cursor.getColumnCount() == 0) {
            deleteTone(tone);
        }
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
