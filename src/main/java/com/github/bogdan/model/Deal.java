package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;

@DatabaseTable(tableName = "deal")
public class Deal implements Filtration{

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private PostApplication postApplication;

    @DatabaseField
    private String startDate;

    @DatabaseField
    private Status status;

    @DatabaseField
    private String endDate;

    public Deal() {
    }

    public Deal( PostApplication postApplication, String startDate, Status status, String endDate) {
        this.postApplication = postApplication;
        this.startDate = startDate;
        this.status = status;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public PostApplication getPostApplication() {
        return postApplication;
    }

    public void setPostApplication(PostApplication postApplication) {
        this.postApplication = postApplication;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deal deal = (Deal) o;
        return id == deal.id &&
                Objects.equals(postApplication, deal.postApplication) &&
                Objects.equals(startDate, deal.startDate) &&
                status == deal.status &&
                Objects.equals(endDate, deal.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postApplication, startDate, status, endDate);
    }

    @Override
    public ArrayList<String> getQueryParams() {
        return null;
    }
}
