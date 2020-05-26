package com.github.bogdan.service;


import com.github.bogdan.exception.WebException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

}
