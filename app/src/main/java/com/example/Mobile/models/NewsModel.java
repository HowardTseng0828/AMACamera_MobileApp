package com.example.Mobile.models;

import java.io.Serializable;

public class NewsModel implements Serializable {

    public String title,content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
