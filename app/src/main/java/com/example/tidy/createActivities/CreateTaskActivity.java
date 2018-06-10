package com.example.tidy.createActivities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tidy.R;
import com.example.tidy.objects.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.tidy.Utils.getDatabase;

public class CreateTaskActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.due_date_tv)
    TextView dueDate;
    @BindView(R.id.pick_date_button)
    Button pickDate;
    @BindView(R.id.fab_save)
    FloatingActionButton fabSave;
    @BindView(R.id.et_task_title)
    EditText taskTitleEditText;
    @BindView(R.id.et_task_details)
    EditText taskDetailsEditText;

    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        getDatabase();

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        pickDate.setOnClickListener(this);
        fabSave.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create New Task");
    }


    @Override
    public void onClick(View view) {
        if (view == pickDate) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTaskActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String selectedDate = dayOfMonth +"."+ (monthOfYear + 1) + "." + year ;


                            dueDate.setText(selectedDate);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if(view == fabSave) {
            saveTodo();
        }
    }

    void saveTodo() {
        // first section
        // get the data to save in our firebase db

        String dateString = dueDate.getText().toString();
        //make the modal object and convert it into hasmap

        //second section
        //save it to the firebase db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("taskList").push().getKey();

        Task task = new Task();
        task.setTitle(taskTitleEditText.getText().toString());
        task.setContent(taskDetailsEditText.getText().toString());
        task.setDate(dateString);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( key, task.toFirebaseObject());
        database.getReference("taskList").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    finish();
                }
            }
        });
    }
}
