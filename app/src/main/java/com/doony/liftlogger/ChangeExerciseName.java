package com.doony.liftlogger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class ChangeExerciseName extends AppCompatActivity {

    private String m_exerciseID;
    private String m_exerciseName;

    ExerciseDatabaseHelper m_exerciseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_exercise_name);

        Intent i = getIntent();
        m_exerciseID = i.getStringExtra("ID");
        m_exerciseDB = new ExerciseDatabaseHelper(this);
        m_exerciseName = m_exerciseDB.getNameFromID(m_exerciseID);

        EditText editText = findViewById(R.id.editTextNewName);
        editText.setText(m_exerciseName);

        setTitle(m_exerciseName);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void pressSaveName(View v) {
        EditText editText = findViewById(R.id.editTextNewName);
        String newName = String.valueOf(editText.getText());
        if(newName != m_exerciseName) {
            m_exerciseDB.changeName(m_exerciseID, newName);
        }
        finish();
    }
}