package com.github.bogdan;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.Deadline;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.bogdan.service.LocalDateService.checkDeadline;
import static com.github.bogdan.service.SortingService.sortByQueryParams;

public class Test {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException, SQLException {

    }
}
