package com.kurtheiligmann.okno.data;

import com.orm.SugarRecord;

/**
 * Created by kurtheiligmann on 2/11/15.
 */
public class Message extends SugarRecord<Message> {
    private String body;
    private String audioFilePath;
    private boolean caseSensitive;

    public Message() {
    }

    public Message(String body, String audioFilePath, boolean caseSensitive) {
        this.body = body;
        this.audioFilePath = audioFilePath;
        this.caseSensitive = caseSensitive;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}
