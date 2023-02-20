package com.example.liftlogger;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ViewExercise extends AppCompatActivity {

    private String m_exerciseID;
    private Date m_currentTime;
    ExerciseDatabaseHelper m_exerciseDB;

    ArrayList<SetListItem> setList;
    ArrayList<Calendar> dateList;

    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        Intent i = getIntent();
        m_exerciseID = i.getStringExtra("ID");

        m_currentTime = Calendar.getInstance().getTime();

        m_exerciseDB = new ExerciseDatabaseHelper(this);

        // Initialize ArrayLists
        setList = new ArrayList<>();
        dateList = new ArrayList<>();

        alertBuilder = new AlertDialog.Builder(this);

        displaySets();

        // Set page title
        String exerciseName = m_exerciseDB.getNameFromID(m_exerciseID);
        setTitle(exerciseName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displaySets();
        String exerciseName = m_exerciseDB.getNameFromID(m_exerciseID);
        setTitle(exerciseName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_exercise_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.app_bar_edit) {
            Intent i = new Intent(this, ChangeExerciseName.class);
            i.putExtra("ID", m_exerciseID);
            startActivity(i);
        }

        if (id == R.id.app_bar_delete) {
            alertBuilder.setTitle("Delete Exercise");
            alertBuilder.setMessage("Are you sure you want to delete this exercise? All related sets will also be deleted. This action can not be undone.");
            alertBuilder.setCancelable(true);
            alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //delete the exercise and return to parent activity
                    m_exerciseDB.deleteExercise(m_exerciseID);
                    finish();
                }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alertBuilder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void displaySets() {
        dateList.clear();

        Cursor cursor = m_exerciseDB.readExerciseSets(m_exerciseID);
        if(cursor.getCount() == 0) {
            //Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                long timestamp = cursor.getLong(1); // 1: time
                Date date_timestamp = new Date(timestamp);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date_timestamp);

                int year, month, date;
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                date = cal.get(Calendar.DATE);
                cal.clear();
                cal.set(year, month, date);

                if(!dateList.contains(cal)) {
                    dateList.add(cal);
                }
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDates);
        DaySetListAdapter adapter = new DaySetListAdapter(this, dateList, m_exerciseID);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void pressAddSet(View v) {

        String setWeight;
        setWeight = ((EditText)findViewById(R.id.editTextNumberWeight)).getText().toString();

        String setRepetitions;
        setRepetitions = ((EditText)findViewById(R.id.editTextNumberRepetitions)).getText().toString();

        if(setWeight.isEmpty() && setRepetitions.isEmpty()) {
            Toast.makeText(this, "Failed to Add Empty Set", Toast.LENGTH_SHORT).show();
            return;
        } else if(setWeight.isEmpty()) {
            setWeight = "0";
        } else if(setRepetitions.isEmpty()) {
            setRepetitions = "0";
        }

        // Finding the current time
        m_currentTime = Calendar.getInstance().getTime();
        long timestamp = m_currentTime.getTime();

        // Adding exercise to database
        m_exerciseDB.addSet(m_exerciseID, timestamp, setWeight, setRepetitions);
        refreshSetList();
    }

    private void refreshSetList() {
        displaySets();
    }
}