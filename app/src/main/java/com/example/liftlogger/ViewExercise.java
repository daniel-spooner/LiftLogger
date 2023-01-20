package com.example.liftlogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ViewExercise extends AppCompatActivity {

    private String m_exerciseID;
    private Date m_currentTime;
    ExerciseDatabaseHelper m_exerciseDB;

    ArrayList<String> setIDs, setTimes, setWeights, setRepetitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        Intent i = getIntent();
        m_exerciseID = i.getStringExtra("ID");

        m_currentTime = Calendar.getInstance().getTime();

        m_exerciseDB = new ExerciseDatabaseHelper(this);

        // Initialize ArrayLists
        setIDs = new ArrayList<>();
        setTimes = new ArrayList<>();
        setWeights = new ArrayList<>();
        setRepetitions = new ArrayList<>();

        displaySets();

        // Set page title
        String exerciseName = m_exerciseDB.getNameFromID(m_exerciseID);
        setTitle(exerciseName);
    }

    private void displaySets() {
        setIDs.clear();
        setTimes.clear();
        setWeights.clear();
        setRepetitions.clear();

        Cursor cursor = m_exerciseDB.readExerciseSets(m_exerciseID); // TODO: Change this function to display sets of the given exercise id
        if(cursor.getCount() == 0) {
            //Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                setIDs.add(cursor.getString(0)); // 0: id
                setTimes.add(cursor.getString(1)); // 1: timestamp
                setWeights.add(cursor.getString(3)); // 3: weight
                setRepetitions.add(cursor.getString(4)); // 4: repetitions
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewSets);
        SetListAdapter adapter = new SetListAdapter(this, setIDs, setTimes, setWeights, setRepetitions);
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