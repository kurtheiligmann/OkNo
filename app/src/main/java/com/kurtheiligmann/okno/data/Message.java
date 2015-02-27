package com.kurtheiligmann.okno.data;

import com.orm.SugarRecord;

/**
 * Created by kurtheiligmann on 2/11/15.
 */
public class Message extends SugarRecord<Message> {
    String body;
    Tone tone;

    public Message() {
    }

    public Message(String body, Tone tone) {
        setBody(body);
        setTone(tone);
    }

    @Override
    public void save() {
        getTone().save();
        super.save();
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
