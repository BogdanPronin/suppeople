package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.PostApplication;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostApplicationService {
    static Dao<PostApplication,Integer> postApplicationDao;
    static Dao<Post,Integer> postDao;

    static {
        try {
            postApplicationDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,PostApplication.class);
            postDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Post.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void checkDoesSuchApplicationExist(int userId,int postId) throws SQLException {
        for(PostApplication p:postApplicationDao.queryForAll()){
            if(p.getPost().getId() == postId && p.getUser().getId() == userId){
                throw new WebException("You have already written application for this post",400);
            }
        }
    }

    public static void checkDoesSuchApplicationExist(int postApplicationId) throws SQLException {
        if(postApplicationDao.queryForId(postApplicationId) == null){
            throw new WebException("Such application doesn't exist",400);
        }
    }

    public static void checkIsItUsersApplication(int applicationId,int userId) throws SQLException {
        if(postApplicationDao.queryForId(applicationId).getUser().getId() == userId){
            throw new WebException("It isn't your application",400);
        }
    }

    public static PostApplication getPostApplication(int postApplicationId) throws SQLException {
        return postApplicationDao.queryForId(postApplicationId);
    }

    public static ArrayList<PostApplication> getPostApplications(int postId) throws SQLException {
        ArrayList<PostApplication> postApplications = new ArrayList<>();
        for(PostApplication p: postApplicationDao.queryForAll()){
            if(p.getPost().getId() == postId){
                postApplications.add(p);
            }
        }
        return postApplications;
    }

    public static void checkItIsNotUserPost(User user, int postId) throws SQLException {
        if(postDao.queryForId(postId).getUser().equals(user)){
            throw new WebException("Вы не можете отправлять предложения к своим постам",404);
        }
    }

}