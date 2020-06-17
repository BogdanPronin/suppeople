package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;

@DatabaseTable(tableName = "post")
public class Post implements Filtration {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private User user;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private AreaOfActivity areaOfActivity;
    @DatabaseField
    private String task;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Deadline deadline;
    @DatabaseField
    private String city;
    @DatabaseField
    private String country;

    public Post() {
    }

    public Post(User user, AreaOfActivity areaOfActivity, String task, Deadline deadline, String city, String country) {
        this.user = user;
        this.areaOfActivity = areaOfActivity;
        this.task = task;
        this.deadline = deadline;
        this.city = city;
        this.country = country;
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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    public void setDeadline(Deadline deadline) {
        this.deadline = deadline;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id &&
                Objects.equals(user, post.user) &&
                Objects.equals(areaOfActivity, post.areaOfActivity) &&
                Objects.equals(task, post.task) &&
                Objects.equals(deadline, post.deadline) &&
                Objects.equals(city, post.city) &&
                Objects.equals(country, post.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, areaOfActivity, task, deadline, city, country);
    }

    @Override
    public ArrayList<String> getQueryParams() {
        return null;
    }
}
