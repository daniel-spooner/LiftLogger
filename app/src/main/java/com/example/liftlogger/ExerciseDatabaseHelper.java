package com.example.liftlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ExerciseDatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Exercises.db";

    private static final String EXERCISE_TABLE = "exercise_names";
    private static final String EXERCISE_TABLE_ID = "id";
    private static final String EXERCISE_TABLE_NAME = "name";

    private static final String SET_TABLE = "exercise_sets";
    private static final String SET_TABLE_ID = "id";
    private static final String SET_TABLE_TIMESTAMP = "timestamp";
    private static final String SET_TABLE_EXERCISE_ID = "exercise_id";
    private static final String SET_TABLE_EXERCISE_NAME = "exercise_name";
    private static final String SET_TABLE_WEIGHT_KG = "weight_kg";
    private static final String SET_TABLE_REPETITIONS = "repetitions";



    public ExerciseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;

        // Create the exercise names table
        query =
                "CREATE TABLE IF NOT EXISTS " + EXERCISE_TABLE + " (" + EXERCISE_TABLE_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " + EXERCISE_TABLE_NAME + " TEXT);";
        db.execSQL(query);

        // Create the exercise sets table
        query =
                "CREATE TABLE IF NOT EXISTS " + SET_TABLE + " (" + SET_TABLE_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " + SET_TABLE_TIMESTAMP + " INTEGER, " +
                        SET_TABLE_EXERCISE_ID + " TEXT, " + SET_TABLE_EXERCISE_NAME + " TEXT, " +
                        SET_TABLE_WEIGHT_KG + " REAL, " + SET_TABLE_REPETITIONS + " INTEGER" + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EXERCISE_TABLE);

    }

    void addExercise(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(EXERCISE_TABLE_NAME, name);

        long result = db.insert(EXERCISE_TABLE, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readExerciseNames() {
        String query = "SELECT * FROM " + EXERCISE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public String getNameFromID(String id) {
        String query = "SELECT * FROM " + EXERCISE_TABLE + " WHERE id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        String name = "";
        if(cursor.moveToNext()) {
            name = cursor.getString(1);
        }
        return name;
    }
}
