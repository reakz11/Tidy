package com.example.tidy.objects;

public class Note {

    private String mTitle;
    private String mContent;

    public Note(String title, String content) {
        this.mTitle = title;
        this.mContent = content;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

}
