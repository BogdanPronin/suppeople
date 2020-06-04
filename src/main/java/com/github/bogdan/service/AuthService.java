package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class AuthService {
    public static boolean authorization(String login, String  password) throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        for(User u:userDao.queryForAll()){
            if(u.getLogin().equals(login) && BCrypt.checkpw(password,u.getPassword())){
                return true;
            }
        }
        return false;
    }
    public static void checkAuthorization(String login, String password, Context ctx) throws SQLException {
        if(!authorization(login,password))
            throw new WebException("Authorization failed",400);
    }
}
