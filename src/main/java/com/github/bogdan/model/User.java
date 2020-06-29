package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Objects;

import static com.github.bogdan.service.ClassService.getFieldsName;

@DatabaseTable(tableName = "user")
public class User implements Filtration {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private Role role;

    @DatabaseField(unique = true)
    private String login;

    @DatabaseField
    private String password;

    @DatabaseField(unique = true)
    private String email;

    @DatabaseField
    private String dateOfBirthday;

    @DatabaseField
    private String dateOfRegister;

    @DatabaseField
    private String phone;

    @DatabaseField
    private String fname;

    @DatabaseField
    private String lname;

    @DatabaseField
    private String city;

    @DatabaseField
    private String country;

    public User() {
    }

    public User(Role role, String login, String password, String email, String dateOfBirthday, String dateOfRegister, String phone, String fname, String lname, String city, String country) {
        this.role = role;
        this.login = login;
        this.password = password;
        this.email = email;
        this.dateOfBirthday = dateOfBirthday;
        this.dateOfRegister = dateOfRegister;
        this.phone = phone;
        this.fname = fname;
        this.lname = lname;
        this.city = city;
        this.country = country;
    }

    public User(Role role, String login, String password, String email, String dateOfBirthday, String dateOfRegister, String phone, String fname, String lname) {
        this.role = role;
        this.login = login;
        this.password = password;
        this.email = email;
        this.dateOfBirthday = dateOfBirthday;
        this.dateOfRegister = dateOfRegister;
        this.phone = phone;
        this.fname = fname;
        this.lname = lname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                role == user.role &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(dateOfBirthday, user.dateOfBirthday) &&
                Objects.equals(dateOfRegister, user.dateOfRegister) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(fname, user.fname) &&
                Objects.equals(lname, user.lname) &&
                Objects.equals(city, user.city) &&
                Objects.equals(country, user.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, login, password, email, dateOfBirthday, dateOfRegister, phone, fname, lname, city, country);
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
        return s;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
