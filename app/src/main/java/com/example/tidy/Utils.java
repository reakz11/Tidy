package com.example.tidy;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private static FirebaseDatabase mDatabase;

    private static String mUserId;

    private static int mCurrentDate;

    private static int mTomorrowDate;

    private static int mYear, mMonth, mDay;

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

    public static int getCurrentDate() {
        if (mCurrentDate == 0) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            Date dateRepresentation = c.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateString = sdf.format(dateRepresentation);
            mCurrentDate = Integer.parseInt(dateString);
        }
        return mCurrentDate;
    }

    public static int getTomorrowDate() {
        if (mTomorrowDate == 0) {
            int tomorrowDate = getCurrentDate() + 2;
            mTomorrowDate = tomorrowDate;
        }
        return mTomorrowDate;
    }
}
