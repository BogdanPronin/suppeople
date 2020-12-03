package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.*;
import com.github.bogdan.utilitis.NewThread;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.github.bogdan.service.PostApplicationService.getPostApplications;

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
    static Logger logger = LoggerFactory.getLogger(PostService.class);

    public static void addPostQt(Post p) throws SQLException {
        ArrayList<PostApplication> postApplications = getPostApplications(p.getId());
        for(PostApplication a:postApplications){
            int qt = a.getUser().getPostsQt();
            if(a.getStatus()== ApplicationStatus.ACCEPTED) {
                a.getUser().setPostsQt(qt + 1);
                userDao.update(a.getUser());
            }
        }

    }

}
