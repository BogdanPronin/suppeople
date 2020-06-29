package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;

@DatabaseTable(tableName = "post_application")
public class PostApplication implements Filtration{
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private User user;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Post post;
    @DatabaseField
    private String message;

    public PostApplication() {
    }

    public PostApplication(User user, Post post, String message) {
        this.user = user;
        this.post = post;
        this.message = message;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostApplication that = (PostApplication) o;
        return id == that.id &&
                Objects.equals(user, that.user) &&
                Objects.equals(post, that.post) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, post, message);
    }

    @Override
    public ArrayList<String> getQueryParams() {
        ArrayList<String> s = new ArrayList<>();
        s.add("id");
        return s;
    }
}
