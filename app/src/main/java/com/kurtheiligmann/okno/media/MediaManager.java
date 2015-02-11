package com.kurtheiligmann.okno.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.kurtheiligmann.okno.R;

import java.util.HashMap;

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
        getAudioFilesByText().put("ok", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("OK", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("oK", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("k", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("kk", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("K", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("KK", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("Kk", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("okay", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("Okay", "android.resource://com.kurtheiligmann.okno/raw/two_tone");
        getAudioFilesByText().put("OKAY", "android.resource://com.kurtheiligmann.okno/raw/two_tone");

        getAudioFilesByText().put("no", "android.resource://com.kurtheiligmann.okno/raw/no_tone");
        getAudioFilesByText().put("No", "android.resource://com.kurtheiligmann.okno/raw/no_tone");
        getAudioFilesByText().put("nope", "android.resource://com.kurtheiligmann.okno/raw/no_tone");
        getAudioFilesByText().put("NO", "android.resource://com.kurtheiligmann.okno/raw/no_tone");
        getAudioFilesByText().put("nO", "android.resource://com.kurtheiligmann.okno/raw/no_tone");
    }

    public void playSound(String smsText) {
        String audioFilePath = getAudioFilesByText().get(smsText);
        if (audioFilePath != null) {
            MediaPlayer player = MediaPlayer.create(getContext(), Uri.parse(audioFilePath));
            player.start();
        }
    }
}
