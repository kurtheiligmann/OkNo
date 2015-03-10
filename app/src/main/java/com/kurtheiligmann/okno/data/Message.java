package com.kurtheiligmann.okno.data;

import android.database.Cursor;

public class Message {
    protected static final String TABLE_NAME = "messages";
    protected static final String COLUMN_ID = "_id";
    protected static final String COLUMN_BODY = "body";
    protected static final String COLUMN_RINGTONE_NAME = "tone_id";

    protected static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_BODY,
            COLUMN_RINGTONE_NAME
    };

    private long id;
    private String body;
    private String ringtoneName;

    public static Message getInstance(String body, String ringtoneName) {
        return Message.getInstance(-1, body, ringtoneName);
    }

    public static Message getInstance(long id, String body, String ringtoneName) {
        Message message = new Message();
        message.setId(id);
        message.setBody(body);
        message.setRingtoneName(ringtoneName);
        return message;
    }

    public static Message getInstance(Cursor cursor) {
        return Message.getInstance(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
    }

    private Message() {

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

    public String getRingtoneName() {
        return ringtoneName;
    }

    public void setRingtoneName(String ringtoneName) {
        this.ringtoneName = ringtoneName;
    }
}
