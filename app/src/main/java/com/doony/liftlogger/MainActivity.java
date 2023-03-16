package com.doony.liftlogger;

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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ExerciseDatabaseHelper exerciseDB;

    ArrayList<ExerciseListItem> exerciseList;
    ExerciseListAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database
        exerciseDB = new ExerciseDatabaseHelper(MainActivity.this);

        // Initialize ArrayLists
        exerciseList = new ArrayList<>();

        // Initialize the recycler view adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        exerciseAdapter = new ExerciseListAdapter(this, exerciseList);
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
            item.setVisible(true);
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayExercises() {
        displayExercises("");
    }

    private void displayExercises(String filter) {

        filter = filter.toLowerCase();

        exerciseList.clear();
        Cursor cursor = exerciseDB.readExercises();

        while(cursor.moveToNext()) {
            //TODO: Don't hardcode indices
            exerciseList.add(new ExerciseListItem(
                    cursor.getString(0), cursor.getString(1), cursor.getInt(2)
            ));
        }

        for(int i = 0; i < exerciseList.size(); i ++) {
            if(!exerciseList.get(i).getName().toLowerCase().startsWith(filter)) {
                exerciseList.remove(i);
                i--;
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(exerciseAdapter);
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