package com.github.bogdan.service;


import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Deadline;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class LocalDateService {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate getLocalDateByString(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(text, formatter);
        return parsedDate;
    }

    public static void checkLocalDateFormat(String text){
        try {
            LocalDate parsedDate = LocalDate.parse(text, formatter);
        }catch (DateTimeParseException e){
            throw new WebException("Wrong date format, correct date format should be YYYY-MM-DD"+'\n'+e.getMessage(),400);
        }
    }

    public static void checkAge(String dateOfBirthday){
        LocalDate date = LocalDate.parse(dateOfBirthday,formatter);
        Period period = Period.between(date,LocalDate.now());
        if(period.getYears() < 18){
            throw new WebException("This service can be used by persons over the age of 18",403);
        }
    }

    public static void checkLocalDateTimeFormat(String text){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime parsedDate = LocalTime.parse(text, formatter);
        }catch (DateTimeParseException e){
            throw new WebException("Wrong time format, correct date format should be HH:mm"+'\n'+e.getMessage(),400);
        }
    }

    public static void checkValidDate(String dateOfEnrollment,String dateOfDrop){
        LocalDate first = LocalDate.parse(dateOfEnrollment);
        LocalDate second = LocalDate.parse(dateOfDrop);
        if(!first.isBefore(second) ||first.equals(second)){
            throw new WebException("Date of enrollment cannot be earlier the date of drop",400);
        }
    }

    public static Deadline checkDeadline(String deadline){
        if(!Pattern.matches("^[0-9]{1,3}[d][0-9]{1,3}[h][0-9]{1,3}[m]$",deadline)){
            throw new WebException("Wrong deadline format, correct format \"$d$h$m\"",400);
        }else{
            Pattern pattern = Pattern.compile("[dhm]");
            String[] a = pattern.split(deadline);
            ArrayList<String> list = new ArrayList<>();
            list.addAll(Arrays.asList(a));
            int days = Integer.parseInt(list.get(0));
            int hours = Integer.parseInt(list.get(1));
            int minutes = Integer.parseInt(list.get(2));
            hours += minutes/60;
            minutes = minutes%60;
            days += hours/24;
            hours = hours%24;
            Deadline deadlineObj = new Deadline(days,minutes,hours);
            return deadlineObj;
        }
    }

    static Dao<Deadline,Integer> deadlineDao;

    static {
        try {
            deadlineDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Deadline.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addDeadlineToBase(Deadline deadline) throws SQLException {
        deadlineDao.create(deadline);
    }

//    public static void checkDoesSuchDeadlineExist(int id){
//
//    }
}
