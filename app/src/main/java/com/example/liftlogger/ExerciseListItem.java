package com.example.liftlogger;

public class ExerciseListItem {
    private String id;
    private String name;
    private int favourite;

    public ExerciseListItem(String id, String name, int favourite) {
        this.id = id;
        this.name = name;
        this.favourite = favourite;
    }

    public String getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getFavourite() { return favourite; }

    public void setID(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setFavourite(int favourite) { this.favourite = favourite; }
}
