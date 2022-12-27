package com.example.liftlogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ExerciseDatabaseHelper exerciseDB;

    ArrayList<String> exerciseIDs, exerciseNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database
        exerciseDB = new ExerciseDatabaseHelper(MainActivity.this);

        // Initialize ArrayLists
        exerciseIDs = new ArrayList<>();
        exerciseNames = new ArrayList<>();

        displayExercises();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ExerciseListAdapter adapter = new ExerciseListAdapter(this, exerciseIDs, exerciseNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void displayExercises() {
        exerciseNames.clear();
        Cursor cursor = exerciseDB.readExerciseNames();
        if(cursor.getCount() == 0) {
            //Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                exerciseIDs.add(cursor.getString(0)); // 0: id
                exerciseNames.add(cursor.getString(1)); // 1: name
            }
        }
    }

    public void pressAddExercise(View v) {
        Intent i = new Intent(this, AddExercise.class);
        startActivity(i);
    }

    public void pressExerciseItem(View v) {
        Intent i = new Intent(this, ViewExercise.class);
        startActivity(i);
    }

}