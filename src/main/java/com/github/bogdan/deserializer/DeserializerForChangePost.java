package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.PostApplication;
import com.github.bogdan.model.Status;
import com.github.bogdan.model.User;
import com.github.bogdan.service.CategoryService;
import com.github.bogdan.service.UserService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.github.bogdan.service.CategoryService.getCategory;
import static com.github.bogdan.service.DeserializerService.*;
import static com.github.bogdan.service.PostApplicationService.getPostApplications;
import static com.github.bogdan.service.PostService.*;

public class DeserializerForChangePost extends StdDeserializer<Post> {
    public DeserializerForChangePost(int postId, int userId,Dao<Post,Integer> postDao,Dao<User,Integer> userDao,Dao<PostApplication,Integer> postApplicationDao) {
        super(Post.class);
        this.postId = postId;
        this.userId = userId;
        this.userDao = userDao;
        this.postDao = postDao;
        this.postApplicationDao = postApplicationDao;
    }
    private Dao<Post,Integer> postDao;
    private Dao<PostApplication,Integer> postApplicationDao;
    private Dao<User,Integer> userDao;
    private int postId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private  int userId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int id) {
        this.postId = id;
    }

    @Override
    public Post deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Post p = new Post();

            p.setId(postId);

            Post postBase = postDao.queryForId(postId);

            checkDoesSuchPostExist(postId,postDao);

            checkPostUser(postId,userId,postDao);

            p.setUser(postBase.getUser());


            p.setDateOfCreate(postBase.getDateOfCreate());

            String  status = getOldStringFieldValue(node,"status",postBase.getStatus().toString());
            if(status.equals(Status.COMPLETED.toString()) &&!getPostApplications(p.getId(),postApplicationDao).isEmpty()){
                addPostQt(p,userDao,postApplicationDao);
            }
            p.setStatus(Status.valueOf(status));
            p.setCategory(postBase.getCategory());
            p.setMessage(postBase.getMessage());
            p.setCity(postBase.getCity());
            return p;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
