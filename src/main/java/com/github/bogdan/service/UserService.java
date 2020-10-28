package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Role;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;

import java.sql.SQLException;

public class UserService {
    static Dao<User, Integer> userDao;

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
    public static void checkIsEmailPhoneNull(String phone,String email){
        if(phone == null && email == null){
            throw new WebException("Одно из полей \"Номер телефона\" или \"Email\" должно быть заполнено",400);
        }
    }


    public static void checkIsUserAdmin(Context ctx) throws SQLException {
        checkIsUserAdmin(getUserById(ctx.basicAuthCredentials().getUsername()));
    }


    public static void checkValidLogin(String login) {
        for(int i = 0; i < login.length();i++){
            if(login.charAt(i) >= 'A' && login.charAt(i) <= 'Z' || login.charAt(i) >= 'a' && login.charAt(i) <= 'z'){}
        }
    }

    public static User getUserById(String login) throws SQLException {
        for(User user: userDao.queryForAll()){
            if(user.getPhone()!=null){
                if(user.getPhone().equals(login)){
                    return user;
                }
            }
            if(user.getEmail()!=null){
                if(user.getEmail().equals(login)){
                    return user;
                }
            }
        }
        return null;
    }

    public static User getUserById(int id) throws SQLException {
        return userDao.queryForId(id);
    }

    public static void checkDoesSuchUserExist(int id) throws SQLException {
        if(userDao.queryForId(id)==null){
            throw new WebException("User with such id isn't exist",400);
        }
    }


}