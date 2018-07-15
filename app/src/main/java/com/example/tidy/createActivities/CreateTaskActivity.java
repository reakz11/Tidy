package com.example.tidy.createActivities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.tidy.R;
import com.example.tidy.objects.Project;
import com.example.tidy.objects.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.tidy.Utils.getCurrentDateAndTime;
import static com.example.tidy.Utils.getDatabase;
import static com.example.tidy.Utils.getUserId;
import static com.example.tidy.Utils.isEmpty;
import static com.example.tidy.Utils.updateWidget;

public class CreateTaskActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.due_date_tv)
    TextView dueDate;
    @BindView(R.id.pick_date_button)
    Button pickDate;
    @BindView(R.id.fab_save_task)
    FloatingActionButton fabSave;
    @BindView(R.id.et_task_title)
    EditText taskTitleEditText;
    @BindView(R.id.et_task_details)
    EditText taskDetailsEditText;
    @BindView(R.id.pick_project_button)
    Button pickProject;
    @BindView(R.id.selected_project_tv)
    TextView selectedProjectTv;

    private String dateDb;
    private List<Project> projectItemList = new ArrayList<>();
    private List<String> projectTitleList = new ArrayList<>();
    private List<Project> projects;
    private DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance()
            .getReference();
    private Project selectedProject;
    private String projectId = "0";
    private String id;

    private SharedPreferences pref;
    private String savedTitle = "saved_title";
    private String savedContent = "saved_content";
    private String savedDate = "saved_date";
    private String savedProject = "saved_project";
    private String savedId = "saved_id";
    private String nothing = "nothing";
    private String savedTitleStr;
    private String savedContentStr;
    private String savedDateStr;
    private String savedProjectStr;
    private String savedIdStr;
    public static final String myPreference = "myPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        getDatabase();

        pref = getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        pickDate.setOnClickListener(this);
        pickProject.setOnClickListener(this);
        fabSave.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.create_new_task);
    }

    @Override
    protected void onResume(){
        super.onResume();

        if (pref.contains(savedTitle)){
            taskTitleEditText.setText(pref.getString(savedTitle, nothing));
        }

        if (pref.contains(savedContent)){
            taskDetailsEditText.setText(pref.getString(savedContent, nothing));
        }

        if (pref.contains(savedDate)){
            dueDate.setText(pref.getString(savedDate, nothing));
        }

        if (pref.contains(savedProject)){
            selectedProjectTv.setText(pref.getString(savedProject, nothing));
        }

        if (pref.contains(savedId)){
            projectId = pref.getString(savedId, nothing);
        }


        mFirebaseDatabase.child("users").child(getUserId())
                .child("projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    projectItemList.clear();
                    List<String> values;
                    @SuppressWarnings("unchecked") Map<String, String> td = (HashMap<String,String>) dataSnapshot.getValue();

                    if (td != null) {
                        values = new ArrayList<>(td.values());
                        projectTitleList.clear();

                        JSONArray jsonArray = new JSONArray(values);
                        String jsonArrayStr = jsonArray.toString();

                        Type listType = new TypeToken<List<Project>>(){}.getType();
                        projects = new Gson().fromJson(jsonArrayStr, listType);

                        final int N = projects.size();
                        for (int i = 0; i < N; i++) {
                            Project projectItem = projects.get(i);
                            String projectStr = projectItem.getTitle();
                            projectTitleList.add(projectStr);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        String dateStr = "";
        String projectTitleStr = "";
        String idStr ="";

        SharedPreferences.Editor editor = pref.edit();

        if (!isEmpty(taskTitleEditText)){
            savedTitleStr = taskTitleEditText.getText().toString();
            editor.putString(savedTitle,savedTitleStr);
        }

        if (!isEmpty(taskDetailsEditText)){
            savedContentStr = taskDetailsEditText.getText().toString();
            editor.putString(savedContent, savedContentStr);
        }

        if (dueDate.getText() != null){
            savedDateStr = dueDate.getText().toString();
            dateStr = savedDateStr;
            editor.putString(savedDate, savedDateStr);
        }

        if (selectedProjectTv.getText() != null){
            savedProjectStr = selectedProjectTv.getText().toString();
            projectTitleStr = savedProjectStr;
            editor.putString(savedProject, savedProjectStr);
        }

        if (!projectId.equals("0")){
            savedIdStr = projectId;
            idStr = savedIdStr;
            editor.putString(savedId, savedIdStr);
        }

        if (!dateStr.equals("")){
            outState.putString(savedDate, dateStr);
        }

        if (!projectTitleStr.equals("")){
            outState.putString(savedProject, projectTitleStr);
        }

        if (!idStr.equals("")){
            outState.putString(savedId, idStr);
        }

        editor.apply();
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState){
        super.onRestoreInstanceState(outState);

        if (outState.containsKey(savedDate)) {
            dueDate.setText(outState.getString(savedDate, nothing));
        }

        if (outState.containsKey(savedDate)) {
            selectedProjectTv.setText(outState.getString(savedProject, nothing));
        }

        if (outState.containsKey(savedId)){
            projectId = outState.getString(savedId, nothing);
        }

        if (pref.contains(savedContent)){
            taskDetailsEditText.setText(pref.getString(savedContent, nothing));
        }

        if (pref.contains(savedProject)){
            selectedProjectTv.setText(pref.getString(savedProject, nothing));
        }

        if (pref.contains(savedId)){
            projectId = pref.getString(savedId, nothing);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == pickDate) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTaskActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String selectedDate = dayOfMonth +"."+ (monthOfYear + 1) + "." + year ;

                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.YEAR, year);
                            cal.set(Calendar.MONTH, monthOfYear);
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date dateRepresentation = cal.getTime();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
                            dateDb = sdf.format(dateRepresentation);
                            dueDate.setText(selectedDate);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if(view == fabSave) {
            if (isEmpty(taskTitleEditText)) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        R.string.no_task_title, Toast.LENGTH_LONG);
                toast.show();
            } else {
                saveTodo();
                Toast toast = Toast.makeText(getApplicationContext(),
                        R.string.task_created, Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        } else if(view == pickProject) {
            new MaterialDialog.Builder(this)
                    .title(R.string.select_project)
                    .items(projectTitleList)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int index, CharSequence text) {
                            selectedProject = projects.get(index);
                            projectId = selectedProject.getId();
                            selectedProjectTv.setText(selectedProject.getTitle());

                            return true;
                        }
                    })
                    .negativeText(R.string.cancel)
                    .positiveText(R.string.ok)
                    .show();
        }
    }

    // get the note data to save in our firebase db
    void saveTodo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            id = user.getUid();
        } else {
            Log.v("Auth", "User ID is null");
        }

        String dateString = dateDb;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("taskList").push().getKey();
        String taskID = getCurrentDateAndTime();

        Task task = new Task();
        task.setTitle(taskTitleEditText.getText().toString());
        task.setContent(taskDetailsEditText.getText().toString());
        task.setDate(dateString);
        task.setState("0");
        if (projectId.equals("0")) {
            task.setProjectId("0");
        } else {
            task.setProjectId(projectId);
        }
        task.setTaskId(taskID);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( key, task.toFirebaseObject());
        database.getReference("users").child(id).child("tasks").updateChildren(childUpdates);
        updateWidget(getApplicationContext());
    }
}
