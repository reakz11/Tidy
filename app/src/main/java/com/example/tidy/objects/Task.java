package com.example.tidy.objects;

import java.text.DateFormat;
import java.util.Date;

public class Task {

    private String mTitle;
    private String mContent;
    private String mDueDate;
    private int mStatus = 0;

    public Task(String title, String date, int status) {
        this.mTitle = title;
        this.mDueDate = date;
        this.mStatus = status;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public String getDueDate() {
        return mDueDate;
    }

    public int getStatus() {
        return mStatus;
    }

}
