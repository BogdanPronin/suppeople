package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

import static com.github.bogdan.service.UserService.getUserById;

public class PostService {
    static Dao<Post,Integer> postDao;
    static Dao<User,Integer> userDao;
    static {
        try {
            postDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Post.class);
            userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,User.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static User getPostUser(int postId) throws SQLException {
        if(postDao.queryForId(postId)!=null){
            return postDao.queryForId(postId).getUser();
        }else throw new WebException("Such post isn't exist",400);
    }

    public static void checkDoesSuchPostExist(int id) throws SQLException {
        if(postDao.queryForId(id) == null){
            throw new WebException("Such post isn't exist",400);
        }
    }

    public static void checkPostUser(int postId, int userId) throws SQLException {

        for(Post post:postDao.queryForAll()){
            if(post.getUser().getId() == userId && post.getId() == postId){
                return;
            }
        }
        throw new WebException("It isn't your post",400);
    }

    public static Post getPost(int postId) throws SQLException {
        return postDao.queryForId(postId);
    }


}
