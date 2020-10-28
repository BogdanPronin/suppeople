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
    private Category category;
    @DatabaseField
    private String message;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Cities city;
    @DatabaseField
    private String image;
    @DatabaseField
    private String dateOfCreate;
    @DatabaseField
    private Status status;

    public Post() {
    }

    public Post(User user, Category category, String message, Cities city, String image, String dateOfCreate,Status status) {
        this.user = user;
        this.category = category;
        this.message = message;
        this.city = city;
        this.image = image;
        this.dateOfCreate = dateOfCreate;
        this.status = status;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Cities getCity() {
        return city;
    }

    public void setCity(Cities city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateOfCreate() {
        return dateOfCreate;
    }

    public void setDateOfCreate(String dateOfCreate) {
        this.dateOfCreate = dateOfCreate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id &&
                Objects.equals(user, post.user) &&
                Objects.equals(category, post.category) &&
                Objects.equals(message, post.message) &&
                Objects.equals(city, post.city) &&
                Objects.equals(image, post.image) &&
                Objects.equals(dateOfCreate, post.dateOfCreate) &&
                status == post.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, category, message, city, image, dateOfCreate, status);
    }

    @Override
    public ArrayList<String> getQueryParams() {
        ArrayList<String> s = new ArrayList<>();
        s.add("city");
        s.add("user");
        s.add("dateOfCreate");
        s.add("status");
        s.add("category");
        return s;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
