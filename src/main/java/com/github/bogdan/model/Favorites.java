package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;

@DatabaseTable(tableName = "favorites")
public class Favorites implements Filtration {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private User user;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Post post;

    public Favorites() {
    }

    public Favorites(User user, Post post) {
        this.user = user;
        this.post = post;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorites favorites = (Favorites) o;
        return id == favorites.id &&
                Objects.equals(user, favorites.user) &&
                Objects.equals(post, favorites.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, post);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public ArrayList<String> getQueryParams() {
        ArrayList<String> s = new ArrayList<>();
        s.add("id");
        s.add("post");
        s.add("user");
        return s;
    }
}
