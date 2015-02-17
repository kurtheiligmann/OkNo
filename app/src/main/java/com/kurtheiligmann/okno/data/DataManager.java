package com.kurtheiligmann.okno.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.kurtheiligmann.okno.R;

import java.util.List;

/**
 * Created by kurtheiligmann on 2/9/15.
 */
public class DataManager {
    public static final String PREFS_NAME = "com.kurtheiligmann.okno.data.prefs";

    private static final String ENABLED_KEY = "com.kurtheiligmann.okno.data.enabledKey";
    private static final String DEFAULT_POSITIVE_SOUND = "android.resource://com.kurtheiligmann.okno/raw/two_tone";
    private static final String DEFAULT_NEGATIVE_SOUND = "android.resource://com.kurtheiligmann.okno/raw/no_tone";

    private Context context;

    public DataManager(Context context) {
        setContext(context);

        List<Message> existingMessages = getAllMessages();
        if (existingMessages == null || existingMessages.size() == 0) {
            initDefaultSounds(getContext());
        }
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
            Message message = new Message(defaultPositiveValue, DEFAULT_POSITIVE_SOUND, false);
            message.save();
        }

        String[] defaultNegativeValues = context.getResources().getStringArray(R.array.default_negative_values);
        for (String defaultNegativeValue : defaultNegativeValues) {
            Message message = new Message(defaultNegativeValue, DEFAULT_NEGATIVE_SOUND, false);
            message.save();
        }
    }

    public List<Message> getAllMessages() {
        List<Message> existingMessages = Message.listAll(Message.class);
        return existingMessages;
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }
}
