package com.example.tidy.objects;

import java.text.DateFormat;
import java.util.Date;

public class Task {

    private String mTitle;
    private String mContent;
    private Date mDueDate;
    private int mStatus = 0;

    public Task(String title, Date date, int status) {
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

    public Date getDueDate() {
        return mDueDate;
    }

    public int getStatus() {
        return mStatus;
    }

}
