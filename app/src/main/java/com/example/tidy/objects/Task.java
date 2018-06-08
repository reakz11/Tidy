package com.example.tidy.objects;

import java.text.DateFormat;

public class Task {

    private String mTitle;
    private String mContent;
    private DateFormat mDueDate;
    private int mStatus = 0;

    public Task(String title, String content, DateFormat date, int status) {
        this.mTitle = title;
        this.mContent = content;
        this.mDueDate = date;
        this.mStatus = status;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public DateFormat getDueDate() {
        return mDueDate;
    }

    public int getStatus() {
        return mStatus;
    }

}
