package com.kurtheiligmann.okno.data;

import com.orm.SugarRecord;

import java.io.File;

/**
 * Created by kurtheiligmann on 2/11/15.
 */
public class Message extends SugarRecord<Message> {
    private String body;
    private String audioFilePath;

    public Message() {
    }

    public Message(String body, String audioFilePath) {
        this.setBody(body);
        this.setAudioFilePath(audioFilePath);
    }

    public String getAudioFileName() {
        File audioFile = new File(getAudioFilePath());
        return audioFile.getName();
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
}
