package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = "user_area")
public class UserArea {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private User user;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private AreaOfActivity areaOfActivity;
    @DatabaseField
    private String experience;
    @DatabaseField
    private double taskExecutionSpeed;

    public UserArea() {
    }

    public UserArea(User user, AreaOfActivity areaOfActivity, String experience, double taskExecutionSpeed) {
        this.user = user;
        this.areaOfActivity = areaOfActivity;
        this.experience = experience;
        this.taskExecutionSpeed = taskExecutionSpeed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AreaOfActivity getAreaOfActivity() {
        return areaOfActivity;
    }

    public void setAreaOfActivity(AreaOfActivity areaOfActivity) {
        this.areaOfActivity = areaOfActivity;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public double getTaskExecutionSpeed() {
        return taskExecutionSpeed;
    }

    public void setTaskExecutionSpeed(double taskExecutionSpeed) {
        this.taskExecutionSpeed = taskExecutionSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserArea userArea = (UserArea) o;
        return id == userArea.id &&
                Double.compare(userArea.taskExecutionSpeed, taskExecutionSpeed) == 0 &&
                Objects.equals(user, userArea.user) &&
                Objects.equals(areaOfActivity, userArea.areaOfActivity) &&
                Objects.equals(experience, userArea.experience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, areaOfActivity, experience, taskExecutionSpeed);
    }
}
