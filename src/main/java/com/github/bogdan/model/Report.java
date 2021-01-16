package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;

@DatabaseTable(tableName = "report")
public class Report implements Filtration {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private User user;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private User reportedUser;
    @DatabaseField
    private String message;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private ReportMessageCategory reportMessageCategory;
    @DatabaseField
    private String image;

    public Report() {
    }

    public Report(User user, User reportedUser, String message, ReportMessageCategory reportMessageCategory, String image) {
        this.user = user;
        this.reportedUser = reportedUser;
        this.message = message;
        this.reportMessageCategory = reportMessageCategory;
        this.image = image;
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

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ReportMessageCategory getReportMessageCategory() {
        return reportMessageCategory;
    }

    public void setReportMessageCategory(ReportMessageCategory reportMessageCategory) {
        this.reportMessageCategory = reportMessageCategory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return id == report.id &&
                Objects.equals(user, report.user) &&
                Objects.equals(reportedUser, report.reportedUser) &&
                Objects.equals(message, report.message) &&
                Objects.equals(image, report.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, reportedUser, message, image);
    }

    @Override
    public ArrayList<String> getQueryParams() {
        ArrayList<String> s = new ArrayList<>();
        s.add("user");
        s.add("reportedUser");
        return s;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
