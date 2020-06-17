package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "deadline")
public class Deadline {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int days;
    @DatabaseField
    private int minutes;
    @DatabaseField
    private int hours;

    public Deadline() {
    }

    public Deadline(int days, int minutes, int hours) {
        this.days = days;
        this.minutes = minutes;
        this.hours = hours;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

}
