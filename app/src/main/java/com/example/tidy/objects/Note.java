package com.example.tidy.objects;

import java.util.HashMap;

public class Note {

    private String mTitle;
    private String mContent;

    public Note() {}

    public Note(String title, String content) {
        this.mTitle = title;
        this.mContent = content;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content){
        mContent = content;
    }

    public HashMap<String,String> toFirebaseObject() {
        HashMap<String,String> note =  new HashMap<String,String>();
        note.put("title", mTitle);
        note.put("content", mContent);

        return note;
    }
}
