package com.kurtheiligmann.okno.data;

import android.database.Cursor;

/**
 * Created by kurtheiligmann on 2/11/15.
 */
public class Message {
    protected static final String TABLE_NAME = "messages";
    protected static final String COLUMN_ID = "_id";
    protected static final String COLUMN_BODY = "body";
    protected static final String COLUMN_TONE_ID = "tone_id";

    protected static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_BODY,
            COLUMN_TONE_ID
    };

    private long id;
    private String body;
    private Tone tone;

    public Message(String body, Tone tone) {
        setId(-1);
        setBody(body);
        setTone(tone);
    }

    public Message(long id, String body, Tone tone) {
        setId(id);
        setBody(body);
        setTone(tone);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Tone getTone() {
        return tone;
    }

    public void setTone(Tone tone) {
        this.tone = tone;
    }

}
