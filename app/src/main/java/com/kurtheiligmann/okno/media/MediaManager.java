package com.kurtheiligmann.okno.media;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kurtheiligmann on 2/10/15.
 */
public class MediaManager {

    private HashMap<String, String> audioFilesByText;
    private Context context;

    private HashMap<String, String> getAudioFilesByText() {
        return audioFilesByText;
    }

    private void setAudioFilesByText(HashMap<String, String> audioFilesByText) {
        this.audioFilesByText = audioFilesByText;
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public MediaManager(Context context) {
        setAudioFilesByText(new HashMap<String, String>());
        setContext(context);

        getAudioFilesByText().put("y", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("Y", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("yes", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("Yes", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("YES", "android.resource://com.kurtheiligmann.okno/raw/two_tone");

        getAudioFilesByText().put("ok", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("oK", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("Ok", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("OK", "android.resource://com.kurtheiligmann.okno/raw/two_tone");

        getAudioFilesByText().put("k", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("K", "android.resource://com.kurtheiligmann.okno/raw/two_tone");

        getAudioFilesByText().put("kk", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("KK", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("kK", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("Kk", "android.resource://com.kurtheiligmann.okno/raw/two_tone");

        getAudioFilesByText().put("okay", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("Okay", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("OKAY", "android.resource://com.kurtheiligmann.okno/raw/two_tone");

        getAudioFilesByText().put("n", "android.resource://com.kurtheiligmann.okno/raw/no_tone");
        getAudioFilesByText().put("N", "android.resource://com.kurtheiligmann.okno/raw/no_tone");
        getAudioFilesByText().put("no", "android.resource://com.kurtheiligmann.okno/raw/no_tone");
        getAudioFilesByText().put("No", "android.resource://com.kurtheiligmann.okno/raw/no_tone");
        getAudioFilesByText().put("NO", "android.resource://com.kurtheiligmann.okno/raw/no_tone");
        getAudioFilesByText().put("nO", "android.resource://com.kurtheiligmann.okno/raw/no_tone");

        getAudioFilesByText().put("nope", "android.resource://com.kurtheiligmann.okno/raw/no_tone");

    }

    public void playSound(String smsText) {
        final String audioFilePath = getAudioFilesByText().get(smsText);
        if (audioFilePath != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MediaPlayer player = MediaPlayer.create(getContext(), Uri.parse(audioFilePath));
                    player.start();
                }
            }, 750);
        }
    }

    public List<Ringtone> getAllRingtones() {
        ArrayList<Ringtone> ringtones = new ArrayList<Ringtone>();
        RingtoneManager ringtoneManager = new RingtoneManager(getContext());
        ringtoneManager.setType(RingtoneManager.TYPE_ALL);

        int numberOfRingtones = ringtoneManager.getCursor().getCount();
        for (int i = 0; i < numberOfRingtones; i++) {
            ringtones.add(ringtoneManager.getRingtone(i));
        }

        return ringtones;
    }
}
