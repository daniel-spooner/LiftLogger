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
    private static final String EXERCISE_TABLE_FAVOURITE = "favourite";

    private static final String SET_TABLE = "exercise_sets";
    private static final String SET_TABLE_ID = "id";
    private static final String SET_TABLE_TIMESTAMP = "timestamp";
    private static final String SET_TABLE_EXERCISE_ID = "exercise_id";
    private static final String SET_TABLE_WEIGHT_KG = "weight_kg";
    private static final String SET_TABLE_REPETITIONS = "repetitions";
    private static final String SET_TABLE_FAVOURITE = "favourite";



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
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " + EXERCISE_TABLE_NAME + " TEXT, " + EXERCISE_TABLE_FAVOURITE + " INTEGER" + ");";
        db.execSQL(query);

        // Create the exercise sets table
        query =
                "CREATE TABLE IF NOT EXISTS " + SET_TABLE + " (" + SET_TABLE_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " + SET_TABLE_TIMESTAMP + " INTEGER, " +
                        SET_TABLE_EXERCISE_ID + " TEXT, " + SET_TABLE_WEIGHT_KG + " REAL, " +
                        SET_TABLE_REPETITIONS + " INTEGER, " + SET_TABLE_FAVOURITE + " INTEGER" + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + EXERCISE_TABLE);

    }

    void addExercise(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(EXERCISE_TABLE_NAME, name);
        cv.put(EXERCISE_TABLE_FAVOURITE, 0); // Initialize favourite as False

        long result = db.insert(EXERCISE_TABLE, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    void addSet(String exercise_id, long timestamp, String weight, String repetitions) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(SET_TABLE_EXERCISE_ID, exercise_id);
        cv.put(SET_TABLE_TIMESTAMP, timestamp);
        cv.put(SET_TABLE_WEIGHT_KG, weight);
        cv.put(SET_TABLE_REPETITIONS, repetitions);
        cv.put(SET_TABLE_FAVOURITE, 0); // Initialize favourite as False

        long result = db.insert(SET_TABLE, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readExercises() {
        String query = "SELECT * FROM " + EXERCISE_TABLE +
                " ORDER BY " + EXERCISE_TABLE_FAVOURITE + " DESC, " + EXERCISE_TABLE_NAME + " ASC ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor readExerciseSets(String exerciseID) {
        String query = "SELECT * FROM " + SET_TABLE + " WHERE " + SET_TABLE_EXERCISE_ID + " = " + exerciseID +
                " ORDER BY " + SET_TABLE_FAVOURITE + " DESC, " + SET_TABLE_TIMESTAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor readExerciseSetsBetweenTimestamps(String exerciseID, long startTime, long endTime) {
        String strStartTime = String.valueOf(startTime);
        String strEndTime = String.valueOf(endTime);
        String query = "SELECT * FROM " + SET_TABLE + " WHERE " + SET_TABLE_EXERCISE_ID + " = " + exerciseID +
                " AND " + SET_TABLE_TIMESTAMP + " >= " + strStartTime + " AND " + SET_TABLE_TIMESTAMP + " < " + strEndTime +
                " ORDER BY " + SET_TABLE_FAVOURITE + " DESC, " + SET_TABLE_TIMESTAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public String getNameFromID(String id) {
        String query = "SELECT * FROM " + EXERCISE_TABLE + " WHERE " + EXERCISE_TABLE_ID + " = " + id;
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

    public boolean getFavouriteExercise(String id) {
        String query = "SELECT " + EXERCISE_TABLE_FAVOURITE + " FROM " + EXERCISE_TABLE + " WHERE " + EXERCISE_TABLE_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        int isFavourite = 0;
        if(cursor.moveToNext()) {
            int colIndex = cursor.getColumnIndex(EXERCISE_TABLE_FAVOURITE);
            if(colIndex >= 0) {
                isFavourite = cursor.getInt(colIndex);
            }
        }
        if(isFavourite == 1) {
            return true;
        }
        return false;
    }

    public void setFavouriteExercise(String id, boolean isFavourite) {
        String value = isFavourite ? "1" : "0";
        String query = "UPDATE " + EXERCISE_TABLE + " SET " + EXERCISE_TABLE_FAVOURITE + " = " + value +
                " WHERE " + EXERCISE_TABLE_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    public void deleteExercise(String id) {
        String query = "DELETE FROM " + EXERCISE_TABLE + " WHERE " + EXERCISE_TABLE_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);

        query = "DELETE FROM " + SET_TABLE + " WHERE " + SET_TABLE_EXERCISE_ID + " = " + id;

        db.execSQL(query);
    }

    public void changeName(String id, String newName) {
        String query = "UPDATE " + EXERCISE_TABLE + " SET " + EXERCISE_TABLE_NAME + " = " + "\"" + newName + "\"" +
                " WHERE " + EXERCISE_TABLE_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }
}