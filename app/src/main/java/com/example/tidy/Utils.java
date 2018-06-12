package com.example.tidy;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Utils {

    private static FirebaseDatabase mDatabase;

    private static String mUserId;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public static String getUserId() {
        if (mUserId == null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            mUserId = user.getUid();
        }
        return mUserId;
    }
}
