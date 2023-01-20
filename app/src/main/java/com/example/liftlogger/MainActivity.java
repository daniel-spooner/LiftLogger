package com.example.liftlogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ExerciseDatabaseHelper exerciseDB;

    ArrayList<String> exerciseIDs, exerciseNames;
    ExerciseListAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database
        exerciseDB = new ExerciseDatabaseHelper(MainActivity.this);

        // Initialize ArrayLists
        exerciseIDs = new ArrayList<>();
        exerciseNames = new ArrayList<>();

        // Initialize the recycler view adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        exerciseAdapter = new ExerciseListAdapter(this, exerciseIDs, exerciseNames);
        recyclerView.setAdapter(exerciseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayExercises();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Exercises");

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                displayExercises(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_settings) {
            pressSettings(item.getActionView());
            item.setVisible(false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayExercises() {
        displayExercises("");
    }

    private void displayExercises(String filter) {
        exerciseNames.clear();
        exerciseIDs.clear();
        Cursor cursor = exerciseDB.readExerciseNames();
        if(cursor.getCount() == 0) {
            //Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                exerciseIDs.add(cursor.getString(0)); // 0: id
                exerciseNames.add(cursor.getString(1)); // 1: name
            }
        }

        filter = filter.toLowerCase();
        for(int i = 0; i < exerciseNames.size(); i ++) {
            if(!exerciseNames.get(i).toLowerCase().startsWith(filter)) {
                exerciseIDs.remove(i);
                exerciseNames.remove(i);
                i--;
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(exerciseAdapter);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void pressAddExercise(View v) {
        Intent i = new Intent(this, AddExercise.class);
        startActivity(i);
    }

    public void pressSettings(View v) {
        Intent i = new Intent(this, Settings.class);
        startActivity(i);
    }

}