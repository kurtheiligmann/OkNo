package com.kurtheiligmann.okno.data;

import com.orm.SugarRecord;

/**
 * Created by kurt on 2/23/15.
 */
public class Tone {
    protected static final String TABLE_NAME = "tones";
    protected static final String COLUMN_ID = "_id";
    protected static final String COLUMN_TITLE = "title";
    protected static final String COLUMN_FILE_ADDRESS= "file_address";

    protected static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_TITLE,
            COLUMN_FILE_ADDRESS
    };

    long id;
    String title;
    String fileAddress;

    public Tone(long id, String title, String fileAddress) {
        setId(id);
        setTitle(title);
        setFileAddress(fileAddress);
    }

    public Tone(String title, String fileAddress) {
        setId(-1);
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
