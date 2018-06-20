package com.example.tidy;

import android.text.format.DateUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private static FirebaseDatabase mDatabase;

    private static String mUserId;

    private static int mCurrentDate;

    private static String mCurrentDateAndTime;

    private static int mTomorrowDate;

    private static int mOtherTimeValue;

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

    public static String getCurrentDateAndTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            mCurrentDateAndTime = sdf.format(new Date());
            return mCurrentDateAndTime;
    }

    public static class DateUtil
    {
        public static Date addDays(Date date, int days)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days); //minus number would decrement the days
            return cal.getTime();
        }
    }

    public static int getTomorrowDate() {
        String sourceDate = String.valueOf(getCurrentDate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date myDate;

        try {
            myDate = sdf.parse(sourceDate);
            myDate = DateUtil.addDays(myDate, 1);
            String dateString = sdf.format(myDate);
            mTomorrowDate = Integer.parseInt(dateString);

        } catch (ParseException e){
            Log.v("DATE", "getTomorrowDate parsing error");
        }

        return mTomorrowDate;
    }

    public static int getOtherTimeValue() {
        String sourceDate = String.valueOf(getCurrentDate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date myDate;

        try {
            myDate = sdf.parse(sourceDate);
            myDate = DateUtil.addDays(myDate, 2);
            String dateString = sdf.format(myDate);
            mOtherTimeValue = Integer.parseInt(dateString);

        } catch (ParseException e){
            Log.v("DATE", "getTomorrowDate parsing error");
        }
        return mOtherTimeValue;
    }
}
