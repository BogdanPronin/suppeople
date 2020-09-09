package com.github.bogdan.databaseConfiguration;

import com.github.bogdan.model.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseConfiguration {
    public static ConnectionSource connectionSource;
    static {
        try{
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:/Users/bogdan/Desktop/suppeople.db");
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
            TableUtils.createTableIfNotExists(connectionSource, Post.class);
            TableUtils.createTableIfNotExists(connectionSource, PostApplication.class);
            TableUtils.createTableIfNotExists(connectionSource, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
