package com.kurtheiligmann.okno.media;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.kurtheiligmann.okno.R;
import com.kurtheiligmann.okno.data.DataManager;
import com.kurtheiligmann.okno.data.Message;
import com.kurtheiligmann.okno.data.OkNoTone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediaManager {

    public static final String DEFAULT_POSITIVE_TITLE = "OkYes";
    public static final String DEFAULT_NEGATIVE_TITLE = "OkNo";
    private static final String SD_CARD_RINGTONE_PATH = Environment.getExternalStorageDirectory().getPath() + "/media/audio/ringtones/";
    private Context context;
    private static List<OkNoTone> allTones;


    public MediaManager(Context context) {
        setContext(context);
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public void playToneForMessageBody(String messageBody) {
        DataManager dataManager = new DataManager(getContext());
        Message message = dataManager.getMessageWithBody(messageBody.toLowerCase());
        dataManager.close();
        if (message != null) {
            final OkNoTone tone = getRingtoneForName(message.getRingtoneName());
            if (tone != null) {
                Log.i(this.getClass().getName(), tone.toString());
                MediaPlayer player = new MediaPlayer();
                int duration = 1500;
                try {
                    player.setDataSource(getContext(), tone.getUri());
                    player.prepare();
                    duration = player.getDuration();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tone.getRingtone().play();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tone.getRingtone().stop();
                    }
                }, duration);
            }

        }
    }

    public List<OkNoTone> getAllTones() {
        if (allTones == null) {
            allTones = Collections.synchronizedList(new ArrayList<OkNoTone>());

            RingtoneManager ringtoneManager = new RingtoneManager(getContext());
            ringtoneManager.setType(RingtoneManager.TYPE_ALL);

            int numberOfRingtones = ringtoneManager.getCursor().getCount();
            for (int i = 0; i < numberOfRingtones; i++) {
                Ringtone ringtone = ringtoneManager.getRingtone(i);
                Log.i(this.getClass().toString(), ringtoneManager.getRingtoneUri(i).toString());
                allTones.add(OkNoTone.getInstance(ringtone, ringtone.getTitle(getContext()), ringtoneManager.getRingtoneUri(i)));
            }
        }

        return allTones;
    }

    public OkNoTone getRingtoneForName(String name) {
        OkNoTone foundRingtone = null;
        OkNoTone defaultRingtone = null;
        List<OkNoTone> allTones = getAllTones();
        for (OkNoTone tone : allTones) {
            String ringtoneTitle = tone.getName();
            if (ringtoneTitle.equals(name)) {
                foundRingtone = tone;
                break;
            } else if (ringtoneTitle.equals(DEFAULT_POSITIVE_TITLE)) {
                defaultRingtone = tone;
            }
        }

        if (foundRingtone == null) {
            foundRingtone = defaultRingtone;
        }

        return foundRingtone;
    }

    public void createOkNoRingtones() {
        int[] soundResourceIds = new int[]{R.raw.two_tone, R.raw.no_tone};
        String[] soundResourceTitles = new String[]{DEFAULT_POSITIVE_TITLE, DEFAULT_NEGATIVE_TITLE};

        for (int i = 0; i < soundResourceIds.length; i++) {
            byte[] buffer = null;

            try {
                InputStream in = getContext().getResources().openRawResource(soundResourceIds[i]);
                int size = in.available();
                buffer = new byte[size];
                in.read(buffer);
                in.close();
            } catch (IOException e) {
                Log.e(this.getClass().getName(), e.getLocalizedMessage());
            }

            String filename = soundResourceTitles[i] + ".m4a";

            boolean exists = (new File(SD_CARD_RINGTONE_PATH)).exists();
            if (!exists) {
                new File(SD_CARD_RINGTONE_PATH).mkdirs();
            }

            try {
                File file = new File(SD_CARD_RINGTONE_PATH + filename);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(file);
                out.write(buffer);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                Log.e(this.getClass().getName(), e.getLocalizedMessage());
            } catch (IOException e) {
                Log.e(this.getClass().getName(), e.getLocalizedMessage());
            }

            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + SD_CARD_RINGTONE_PATH + filename)));

            File k = new File(SD_CARD_RINGTONE_PATH, filename);

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
            values.put(MediaStore.MediaColumns.TITLE, soundResourceTitles[i]);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/m4a");
            values.put(MediaStore.Audio.Media.ARTIST, "okno ");
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            values.put(MediaStore.Audio.Media.IS_ALARM, false);
            values.put(MediaStore.Audio.Media.IS_MUSIC, false);

            getContext().getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath()), values);
        }
    }
}
