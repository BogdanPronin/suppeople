package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class PostService {
    static Dao<Post,Integer> postDao;

    static {
        try {
            postDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Post.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static User getPostUser(int postId) throws SQLException {
        if(postDao.queryForId(postId)!=null){
            return postDao.queryForId(postId).getUser();
        }else throw new WebException("Such post isn't exist",400);
    }
}
