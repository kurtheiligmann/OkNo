package com.kurtheiligmann.okno;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.kurtheiligmann.okno.data.DataManager;
import com.kurtheiligmann.okno.data.Message;
import com.kurtheiligmann.okno.listener.SMSListener;

import java.util.List;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        SMSListener.setContext(this);
        Preference enablePreference = findPreference(getResources().getString(R.string.pref_key_enable_okno));

        final DataManager dataManager = new DataManager(this);
        boolean enabled = dataManager.getEnabled();

        enablePreference.setDefaultValue(enabled);
        enablePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof Boolean) {
                    boolean enabled = (Boolean) newValue;
                    Log.i("enablePreference", "onPreferenceChange:" + newValue);
                    dataManager.saveEnabled(enabled);
                }
                return true;
            }
        });

        ListPreference wordList = (ListPreference) findPreference("word_list");
        List<Message> allMessages = dataManager.getAllMessages();
        CharSequence[] messageTextValues = new CharSequence[allMessages.size()];
        for (int i = 0; i < allMessages.size(); i++) {
            messageTextValues[i] = allMessages.get(i).getBody();
        }
        wordList.setEntryValues(messageTextValues);

    }
}
