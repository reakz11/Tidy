package com.example.tidy.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.tidy.R;
import com.example.tidy.objects.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.tidy.Utils.getCurrentDate;
import static com.example.tidy.Utils.getDatabase;
import static com.example.tidy.Utils.getUserId;


public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private List<Task> taskItemList = new ArrayList<>();
    private Context context = null;

    private Context mContext;
    private int appWidgetId;
    private DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance()
            .getReference();


    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem(context, appWidgetId);
    }

    // Data for widget
    private void populateListItem(Context context, final int appWidgetId) {

        mContext = context;

        getDatabase();

        //Gets data from DB and listens for changes in DB
        mFirebaseDatabase
                .child("users")
                .child(getUserId())
                .child("tasks")
                .orderByChild("date")
                .startAt("!")
                .endAt(String.valueOf(getCurrentDate()))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Map<String, String> td = (HashMap<String, String>) dataSnapshot.getValue();
                    List<String> values = new ArrayList<>(td.values());
                    JSONArray jsonArray = new JSONArray(values);
                    String jsonArrayStr = jsonArray.toString();

                    Type listType = new TypeToken<List<Task>>(){}.getType();
                    List<Task> taskList = new ArrayList<>();
                    taskList = new Gson().fromJson(jsonArrayStr, listType);
                    taskItemList.clear();

                    final int N = taskList.size();
                    for (int i = 0; i < N; i++) {
                        int position = i;
                        Task taskItem = taskList.get(position);
                        if (taskItem.getState().equals("0")) {
                            taskItemList.add(taskItem);
                        }
                    }

                    AppWidgetManager.getInstance(mContext).notifyAppWidgetViewDataChanged(appWidgetId, R.id.listViewWidget);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.v("Widget", "Data set changed");
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return taskItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.list_row);
        Task taskItem = taskItemList.get(position);

        remoteView.setTextViewText(R.id.heading, taskItem.getTitle());
        remoteView.setTextViewText(R.id.content, taskItem.getContent());

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return taskItemList.size();
    }
}