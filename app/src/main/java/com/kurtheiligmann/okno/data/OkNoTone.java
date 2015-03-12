package com.kurtheiligmann.okno.data;

import android.media.Ringtone;
import android.net.Uri;

/**
 * Created by kurtheiligmann on 3/11/15.
 */
public class OkNoTone {

    private Ringtone ringtone;
    private String name;
    private Uri uri;

    private OkNoTone() {

    }

    public static OkNoTone getInstance(Ringtone ringtone, String name, Uri uri) {
        OkNoTone tone = new OkNoTone();
        tone.setRingtone(ringtone);
        tone.setName(name);
        tone.setUri(uri);
        return tone;
    }

    public Ringtone getRingtone() {
        return ringtone;
    }

    private void setRingtone(Ringtone ringtone) {
        this.ringtone = ringtone;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    private void setUri(Uri uri) {
        this.uri = uri;
    }
}
