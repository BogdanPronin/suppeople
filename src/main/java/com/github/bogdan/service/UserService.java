package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.AreaOfActivity;
import com.github.bogdan.model.Role;
import com.github.bogdan.model.User;
import com.github.bogdan.model.UserArea;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserService {
    static Dao<User, Integer> userDao;
    public static Dao<UserArea,Integer> userAreaDao;

    static {
        try {
            userAreaDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserArea.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
    public static boolean checkBooleanIsUserAdmin(int userId) throws SQLException {
        if(userDao.queryForId(userId).getRole() == Role.ADMIN){
            return true;
        }
        return false;
    }
    public static void checkIsUserAdmin(int userId) throws SQLException {
        if(userDao.queryForId(userId).getRole() != Role.ADMIN){
            throw new WebException("You aren't admin",400);
        }
    }

    public static ArrayList<UserArea> getUsersAreas(User user) throws SQLException {
        ArrayList<UserArea> areaOfActivities = new ArrayList<>();
        for(UserArea u:userAreaDao.queryForAll()){
            if(u.getUser().getId() == user.getId()){
                areaOfActivities.add(u);
            }
        }
        return areaOfActivities;
    }

    public static void checkIsUserAdmin(Context ctx) throws SQLException {
        checkIsUserAdmin(getUser(ctx.basicAuthCredentials().getUsername()));
    }

    public static void checkIsLoginInUse(String login) throws SQLException {
        for(User user:userDao.queryForAll()){
            if(user.getLogin().equals(login)){
                throw new WebException("This login is already in use",400);
            }
        }
    }

    public static void checkIsLoginInUse(String login,int userId) throws SQLException {
        for(User user:userDao.queryForAll()){
            if(user.getLogin().equals(login) && user.getId() != userId){

                throw new WebException("This login is already in use",400);
            }
        }
    }

    public static User getUser(String login) throws SQLException {
        for(User user: userDao.queryForAll()){
            if(user.getLogin().equals(login)){
                return user;
            }
        }
        return null;
    }

    public static User getUser(int id) throws SQLException {
        return userDao.queryForId(id);
    }

    public static void checkDoesSuchUserExist(int id) throws SQLException {
        if(userDao.queryForId(id)==null){
            throw new WebException("User with such id isn't exist",400);
        }
    }


}
