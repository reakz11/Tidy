package com.example.tidy.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Task {

    private String mTitle;
    private String mContent;
    private String mDate;

    public Task() {}

    public Task(String title, String content, String date) {
        this.mTitle = title;
        this.mContent = content;
        this.mDate = date;

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

    public void setContent(String content) {
        mContent = content;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getFormattedDate() {

        DateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date;

        try {
            date = originalFormat.parse(mDate);
        } catch (ParseException e){
            return null;
        }
        String formattedDate = targetFormat.format(date);
        return formattedDate;
    }


    public HashMap<String,String> toFirebaseObject() {
        HashMap<String,String> todo =  new HashMap<String,String>();
        todo.put("title", mTitle);
        todo.put("content", mContent);
        todo.put("date", mDate);

        return todo;
    }

}
