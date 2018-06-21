package com.example.tidy.objects;

import java.util.HashMap;

public class Project {

    private String mTitle;
    private String mId;

    public Project() {}

    public Project(String title, String id) {
        this.mTitle = title;
        this.mId = id;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }



    public HashMap<String,String> toFirebaseObject() {
        HashMap<String,String> project =  new HashMap<String,String>();
        project.put("title", mTitle);
        project.put("id", mId);

        return project;
    }
}
