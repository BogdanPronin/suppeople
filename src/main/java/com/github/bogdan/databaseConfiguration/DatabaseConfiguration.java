package com.github.bogdan.databaseConfiguration;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class DatabaseConfiguration {
    public static ConnectionSource connectionSource;
    static {
        try{
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:/Users/bogdan/Desktop/freelance.db");
//            TableUtils.createTableIfNotExists(connectionSource, User.class);
//            TableUtils.createTableIfNotExists(connectionSource, Subject.class);
//            TableUtils.createTableIfNotExists(connectionSource, Group.class);
//            TableUtils.createTableIfNotExists(connectionSource, Schedule.class);
//            TableUtils.createTableIfNotExists(connectionSource, UserGroup.class);
//            TableUtils.createTableIfNotExists(connectionSource, Attendance.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
