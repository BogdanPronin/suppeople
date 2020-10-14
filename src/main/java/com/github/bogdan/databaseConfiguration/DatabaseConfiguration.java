package com.github.bogdan.databaseConfiguration;

import com.github.bogdan.model.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import static com.github.bogdan.databaseConfiguration.DatabasePath.getBagaPath;

public class DatabaseConfiguration {
    public static ConnectionSource connectionSource;
    static {
        try{
            connectionSource = new JdbcConnectionSource(getBagaPath());
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
            TableUtils.createTableIfNotExists(connectionSource, Post.class);
            TableUtils.createTableIfNotExists(connectionSource, PostApplication.class);
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Cities.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
