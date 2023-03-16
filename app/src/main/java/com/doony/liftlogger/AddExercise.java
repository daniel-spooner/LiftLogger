package com.doony.liftlogger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddExercise extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
    }

    public void pressCreateExercise(View v) {
        String exerciseName;
        exerciseName = ((EditText)findViewById(R.id.editTextExerciseName)).getText().toString();

        // Adding exercise to database
        ExerciseDatabaseHelper eDB = new ExerciseDatabaseHelper(AddExercise.this);
        eDB.addExercise(exerciseName.trim());
        finish();
    }
}