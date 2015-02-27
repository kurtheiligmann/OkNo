package com.kurtheiligmann.okno.data;

import com.orm.SugarRecord;

/**
 * Created by kurt on 2/23/15.
 */
public class Tone extends SugarRecord<Tone> {
    String title;
    String fileAddress;

    public Tone() {
    }

    public Tone(String title, String fileAddress) {
        setTitle(title);
        setFileAddress(fileAddress);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = false;
        if (o instanceof Tone) {
            Tone otherTone = (Tone) o;
            equals = getTitle().equals(otherTone.getTitle()) && getFileAddress().equals(otherTone.getFileAddress());
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode() * getFileAddress().hashCode();
    }

}
