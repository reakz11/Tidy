package com.example.tidy.objects;

import java.util.HashMap;

public class Task {

    private String title;
    private String content;
    private String date;

    public Task() {

    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public HashMap<String,String> toFirebaseObject() {
        HashMap<String,String> todo =  new HashMap<String,String>();
        todo.put("title", title);
        todo.put("content", content);
        todo.put("date", date);

        return todo;
    }

}
