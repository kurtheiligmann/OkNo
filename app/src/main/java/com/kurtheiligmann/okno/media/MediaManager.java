package com.kurtheiligmann.okno.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.kurtheiligmann.okno.data.DataManager;
import com.kurtheiligmann.okno.data.Message;
import com.kurtheiligmann.okno.data.Tone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kurtheiligmann on 2/10/15.
 */
public class MediaManager {

    private Context context;

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public MediaManager(Context context) {
        setContext(context);
    }

    public void playToneForMessageBody(String messageBody) {
        Message message = new DataManager(getContext()).getMessageWithBody(messageBody);
        if (message != null) {
            final Tone tone = message.getTone();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Uri toneUri = Uri.parse(tone.getFileAddress());
                    MediaPlayer player = MediaPlayer.create(getContext(), toneUri);
                    player.start();


                }
            }, 750);
        }
    }

    public List<Tone> getAllTones() {
        ArrayList<Tone> tones = new ArrayList<>();
        RingtoneManager ringtoneManager = new RingtoneManager(getContext());
        ringtoneManager.setType(RingtoneManager.TYPE_ALL);

        int numberOfRingtones = ringtoneManager.getCursor().getCount();
        for (int i = 0; i < numberOfRingtones; i++) {
            Ringtone ringtone = ringtoneManager.getRingtone(i);
            Log.i(this.getClass().toString(), ringtoneManager.getRingtoneUri(i).toString());
            tones.add(new Tone(ringtone.getTitle(getContext()), ringtoneManager.getRingtoneUri(i).toString()));
        }

        return tones;
    }
}
