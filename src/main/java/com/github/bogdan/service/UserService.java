package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Role;
import com.github.bogdan.model.User;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.apache.commons.validator.routines.EmailValidator;

import java.sql.SQLException;

public class UserService {
    public static Dao<User, Integer> userDao;
    static {
        try {
            userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void checkIsUserAdmin(User user){
        if(user.getRole() != Role.ADMIN){
            throw new WebException("You aren't admin",400);
        }
    }

    public static void checkIsLoginInUse(String login) throws SQLException {
        for(User user:userDao.queryForAll()){
            if(user.getLogin().equals(login)){
                throw new WebException("This login is already in use",400);
            }
        }
    }


}
