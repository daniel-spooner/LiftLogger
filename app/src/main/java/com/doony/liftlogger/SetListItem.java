package com.doony.liftlogger;

public class SetListItem {
    private String id;
    private String time;
    private String weight;
    private String repetitions;
    private int favourite;

    public SetListItem(String id, String time, String weight, String repetitions, int favourite) {
        this.id = id;
        this.time = time;
        this.weight = weight;
        this.repetitions = repetitions;
        this.favourite = favourite;
    }

    public String getID() {
        return id;
    }
    public String getTime() {
        return time;
    }
    public String getWeight() {
        return weight;
    }
    public String getRepetitions() {
        return repetitions;
    }
    public int getFavourite() {
        return favourite;
    }

    public void setID(String id) { this.id = id; }
    public void setTime(String time) { this.time = time; }
    public void setWeight(String weight) { this.weight = weight; }
    public void setRepetitions(String repetitions) { this.repetitions = repetitions; }
    public void setFavourite(int favourite) { this.favourite = favourite; }
}
