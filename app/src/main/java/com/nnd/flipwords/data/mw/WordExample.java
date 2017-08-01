package com.nnd.flipwords.data.mw;

import org.parceler.Parcel;

/**
 * Created by Android dev on 7/24/17.
 */

@Parcel
public class WordExample {
    long id;
    String title;
    String text;
    String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
