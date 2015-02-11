package com.kurtheiligmann.okno;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.kurtheiligmann.okno.data.DataManager;
import com.kurtheiligmann.okno.listener.SMSListener;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {
    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        Preference enablePreference = findPreference(getResources().getString(R.string.pref_key_enable_okno));
        boolean enabled = DataManager.getEnabled(this);
        SMSListener.listenerEnabled = enabled;
        enablePreference.setDefaultValue(enabled);
        enablePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof Boolean) {
                    boolean enabled = (Boolean)newValue;
                    SMSListener.listenerEnabled = enabled;
                    Log.i("enablePreference", "onPreferenceChange:" + newValue);
                    DataManager.saveEnabled(enabled, SettingsActivity.this);
                    if (enabled) {
                        startOkNoService();
                    }
                }
                return true;
            }
        });
    }

    private void startOkNoService() {

    }
}
