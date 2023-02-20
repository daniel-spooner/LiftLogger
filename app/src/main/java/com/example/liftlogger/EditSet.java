package com.example.liftlogger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EditSet extends AppCompatActivity {

    private String m_exerciseID;
    private String m_setID;

    ExerciseDatabaseHelper m_exerciseDB;

    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set);

        m_exerciseDB = new ExerciseDatabaseHelper(this);

        alertBuilder = new AlertDialog.Builder(this);

        //setTitle("");

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
                    // TODO: make a db function for deleting sets
                    // m_exerciseDB.deleteExercise(m_exerciseID);
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
}