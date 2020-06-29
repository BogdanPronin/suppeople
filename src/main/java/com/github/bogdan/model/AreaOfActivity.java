package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;

@DatabaseTable(tableName = "area_of_activity")
public class AreaOfActivity implements Filtration{

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true)
    private String name;

    public AreaOfActivity() {
    }

    public AreaOfActivity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AreaOfActivity that = (AreaOfActivity) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public ArrayList<String> getQueryParams() {
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
