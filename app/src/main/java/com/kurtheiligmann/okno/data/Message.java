package com.kurtheiligmann.okno.data;

import com.orm.SugarRecord;

/**
 * Created by kurtheiligmann on 2/11/15.
 */
public class Message extends SugarRecord<Message> {
    String body;
    String audioFilePath;
    boolean caseSensitive;

    public Message() {
    }

    public Message(String body, String audioFilePath, boolean caseSensitive) {
        this.body = body;
        this.audioFilePath = audioFilePath;
        this.caseSensitive = caseSensitive;
    }
}
