package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;


@DatabaseTable(tableName = "user")
public class User implements Filtration {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private Role role;

    @DatabaseField
    private String password;

    @DatabaseField
    private String dateOfBirthday;

    @DatabaseField
    private String dateOfRegister;

    @DatabaseField(unique = true)
    private String email;

    @DatabaseField(unique = true)
    private String phone;

    @DatabaseField
    private boolean phoneIsShown;

    @DatabaseField
    private boolean emailIsShown;

    @DatabaseField
    private String fname;

    @DatabaseField
    private String lname;

    @DatabaseField
    private String city;

    @DatabaseField
    private int postsQt;

    public User() {
    }

    public User(Role role, String password, String dateOfBirthday, String dateOfRegister, String email, String phone, boolean phoneIsShown, boolean emailIsShown, String fname, String lname, String city, int postsQt) {
        this.role = role;
        this.password = password;
        this.dateOfBirthday = dateOfBirthday;
        this.dateOfRegister = dateOfRegister;
        this.email = email;
        this.phone = phone;
        this.phoneIsShown = phoneIsShown;
        this.emailIsShown = emailIsShown;
        this.fname = fname;
        this.lname = lname;
        this.city = city;
        this.postsQt = postsQt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(String dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }

    public String getDateOfRegister() {
        return dateOfRegister;
    }

    public void setDateOfRegister(String dateOfRegister) {
        this.dateOfRegister = dateOfRegister;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPhoneIsShown() {
        return phoneIsShown;
    }

    public void setPhoneIsShown(boolean phoneIsShown) {
        this.phoneIsShown = phoneIsShown;
    }

    public boolean isEmailIsShown() {
        return emailIsShown;
    }

    public void setEmailIsShown(boolean emailIsShown) {
        this.emailIsShown = emailIsShown;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostsQt() {
        return postsQt;
    }

    public void setPostsQt(int postsQt) {
        this.postsQt = postsQt;
    }

    @Override
    public ArrayList<String> getQueryParams(){
        ArrayList<String> s = new ArrayList<>();
        s.add("fname");
        s.add("lname");
        s.add("city");
        s.add("country");
        s.add("dateOfRegister");
        s.add("dateOfBirthday");
        s.add("postsQt");
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                phoneIsShown == user.phoneIsShown &&
                emailIsShown == user.emailIsShown &&
                postsQt == user.postsQt &&
                role == user.role &&
                Objects.equals(password, user.password) &&
                Objects.equals(dateOfBirthday, user.dateOfBirthday) &&
                Objects.equals(dateOfRegister, user.dateOfRegister) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(fname, user.fname) &&
                Objects.equals(lname, user.lname) &&
                Objects.equals(city, user.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, password, dateOfBirthday, dateOfRegister, email, phone, phoneIsShown, emailIsShown, fname, lname, city, postsQt);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
