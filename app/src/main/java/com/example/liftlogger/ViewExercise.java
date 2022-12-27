package com.example.liftlogger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ViewExercise extends AppCompatActivity {

    private String m_exerciseName;
    ExerciseDatabaseHelper exerciseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        exerciseDB = new ExerciseDatabaseHelper(this);

        Intent i = getIntent();
        m_exerciseName = exerciseDB.getNameFromID(i.getStringExtra("ID"));
        setTitle(m_exerciseName);
    }
}