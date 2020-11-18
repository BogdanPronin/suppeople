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
    @DatabaseField
    private ApplicationStatus status;

    public PostApplication() {
    }

    public PostApplication(User user, Post post, String message, ApplicationStatus status) {
        this.user = user;
        this.post = post;
        this.message = message;
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

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostApplication that = (PostApplication) o;
        return id == that.id &&
                Objects.equals(user, that.user) &&
                Objects.equals(post, that.post) &&
                Objects.equals(message, that.message) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, post, message, status);
    }

    @Override
    public ArrayList<String> getQueryParams() {
        ArrayList<String> s = new ArrayList<>();
        s.add("id");
        return s;
    }
}
