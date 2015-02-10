package com.kurtheiligmann.okno.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kurtheiligmann on 2/9/15.
 */
public class DataManager {
    public static final String PREFS_NAME = "com.kurtheiligmann.okno.data.prefs";
    private static final String ENABLED_KEY = "com.kurtheiligmann.okno.data.enabledKey";

    public static void saveEnabled(boolean enabled, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ENABLED_KEY, enabled);
        editor.commit();
    }

    public static boolean getEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(ENABLED_KEY, true);
    }
}
