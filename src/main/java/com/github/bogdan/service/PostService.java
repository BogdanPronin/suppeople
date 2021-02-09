package com.github.bogdan.service;

import com.github.bogdan.controller.MainController;
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

import static com.github.bogdan.service.PostApplicationService.LOGGER;
import static com.github.bogdan.service.PostApplicationService.getPostApplications;

public class PostService {


    public static User getPostUser(int postId,Dao<Post,Integer> postDao) throws SQLException {
        if(postDao.queryForId(postId)!=null){
            return postDao.queryForId(postId).getUser();
        }else throw new WebException("Such post isn't exist",400);
    }
    public static ArrayList<Post> getUsersPosts(int userId,Dao<Post,Integer> postDao) throws SQLException {
        ArrayList<Post> posts = new ArrayList<>();
            for(Post p:postDao.queryForAll()){
                if(p.getUser().getId() == userId){
                    posts.add(p);
                }
            }
            LOGGER.info("User's posts"+String.valueOf(posts));
            return posts;
    }
    public static void checkDoesSuchPostExist(int id,Dao<Post,Integer> postDao) throws SQLException {
        if(postDao.queryForId(id) == null){
            throw new WebException("Such post isn't exist",400);
        }
    }

    public static void checkPostUser(int postId, int userId,Dao<Post,Integer> postDao) throws SQLException {
        for(Post post:postDao.queryForAll()){
            if(post.getUser().getId() == userId && post.getId() == postId){
                return;
            }
        }
        throw new WebException("It isn't your post",400);
    }
    public static boolean checkUserPost(int postId, int userId,Dao<Post,Integer> postDao) throws SQLException {

        for(Post post:postDao.queryForAll()){
            if(post.getUser().getId() == userId && post.getId() == postId){
                return true;
            }
        }
        return false;
    }

    public static Post getPost(int postId,Dao<Post,Integer> postDao) throws SQLException {
        return postDao.queryForId(postId);
    }
    static Logger logger = LoggerFactory.getLogger(PostService.class);

    public static void addPostQt(Post p,Dao<User,Integer> userDao, Dao<PostApplication,Integer> postApplicationDao) throws SQLException {
        ArrayList<PostApplication> postApplications = getPostApplications(p.getId(),postApplicationDao);
        for(PostApplication a:postApplications){
            int qt = a.getUser().getPostsQt();
            if(a.getStatus() == ApplicationStatus.ACCEPTED) {
                a.getUser().setPostsQt(qt + 1);
                userDao.update(a.getUser());
            }
        }

    }

}
