package com.doony.liftlogger;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class EditSet extends AppCompatActivity {

    private String m_setID;

    private String m_exerciseName;

    private String setWeight;
    private String setReps;
    private Long setTimestamp;
    private Calendar dateTime;

    private Calendar newDateTime;

    ExerciseDatabaseHelper m_exerciseDB;

    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set);

        Intent i = getIntent();
        m_setID = i.getStringExtra("ID");
        m_exerciseName = i.getStringExtra("NAME");

        m_exerciseDB = new ExerciseDatabaseHelper(this);

        alertBuilder = new AlertDialog.Builder(this);

        Cursor cursor = m_exerciseDB.readExerciseSet(m_setID);

        if(cursor != null) {
            cursor.moveToNext();
            // 1: time, 3: weight, 4: reps
            setWeight = cursor.getString(3);
            setReps = cursor.getString(4);
            setTimestamp = cursor.getLong(1);
        } else {
            Toast.makeText(this, "Failed to Load Set", Toast.LENGTH_SHORT).show();
            finish();
        }

        Date date_timestamp = new Date(setTimestamp);
        dateTime = Calendar.getInstance();
        dateTime.setTime(date_timestamp);
        displayTime(dateTime);

        newDateTime = (Calendar) dateTime.clone();

        EditText editWeight = findViewById(R.id.editTextEditWeight);
        EditText editReps   = findViewById(R.id.editTextEditReps);

        editWeight.setText(setWeight);
        editReps.setText(setReps);

        setTitle(m_exerciseName);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_set_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
            return true;
        }

        if (id == R.id.app_bar_delete) {
            alertBuilder.setTitle("Delete Exercise");
            alertBuilder.setMessage("Are you sure you want to delete this set?");
            alertBuilder.setCancelable(true);
            alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //delete the set and return to parent activity
                    m_exerciseDB.deleteSet(m_setID);
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

    public void pressSaveChanges(View v) {
        boolean changesMade = false;

        EditText editWeight = findViewById(R.id.editTextEditWeight);
        EditText editReps   = findViewById(R.id.editTextEditReps);

        String newWeight = String.valueOf(editWeight.getText());
        String newReps = String.valueOf(editReps.getText());

        //Check if any changes have actually been made
        if(!dateTime.equals(newDateTime) || !setWeight.equals(newWeight) || !setReps.equals(newReps)) {
            changesMade = true;
        }

        //Update if necessary
        if(changesMade) {
            String newTimestamp = String.valueOf(newDateTime.getTimeInMillis());
            m_exerciseDB.updateExerciseSet(m_setID, newTimestamp, newWeight, newReps);
            Toast.makeText(this, "Successfully Saved Changes", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "No Changes Made", Toast.LENGTH_SHORT).show();
        }

    }

    public void pressEditDate(View v) {
        int year = dateTime.get(Calendar.YEAR);
        int month = dateTime.get(Calendar.MONTH);
        int day = dateTime.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDateTime.set(year, monthOfYear, dayOfMonth);
                displayTime(newDateTime);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, listener, year, month, day);
        datePickerDialog.show();
    }

    public void pressEditTime(View v) {
        int hour = dateTime.get(Calendar.HOUR_OF_DAY);
        int minute = dateTime.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                newDateTime.set(Calendar.HOUR_OF_DAY, hour);
                newDateTime.set(Calendar.MINUTE, minute);
                displayTime(newDateTime);
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, listener, hour, minute, false);
        timePickerDialog.show();
    }

    public void displayTime(Calendar timestamp) {
        TextView tvTimestamp = findViewById(R.id.textViewDateTime);

        String timeText;
        timeText = (String) DateFormat.format("MMMM dd yyyy, hh:mm aa", timestamp.getTimeInMillis());

        tvTimestamp.setText(timeText);
    }
}