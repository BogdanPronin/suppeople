package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.*;
import com.github.bogdan.service.UserService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.DeserializerService.getIntFieldValue;
import static com.github.bogdan.service.DeserializerService.getStringFieldValue;
import static com.github.bogdan.service.PostApplicationService.checkDoesSuchApplicationExist;
import static com.github.bogdan.service.PostApplicationService.checkItIsNotUserPost;
import static com.github.bogdan.service.PostService.checkDoesSuchPostExist;
import static com.github.bogdan.service.PostService.getPost;

public class DeserializerForAddPostApplication extends StdDeserializer<PostApplication> {
    public DeserializerForAddPostApplication(int userId, Dao<User, Integer> userDao,Dao<Post,Integer> postDao,Dao<PostApplication,Integer> postApplicationDao) {
        super(PostApplication.class);
        this.userId = userId;
        this.userDao = userDao;
        this.postDao = postDao;
        this.postApplicationDao = postApplicationDao;
    }
    private Dao<User,Integer> userDao;
    private Dao<Post,Integer> postDao;
    private Dao<PostApplication,Integer> postApplicationDao;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public PostApplication deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        try {
            PostApplication p = new PostApplication();

            p.setUser(UserService.getUserById(userId,userDao));

            int postId = getIntFieldValue(node,"postId");
            checkDoesSuchPostExist(postId,postDao);
            checkItIsNotUserPost(UserService.getUserById(userId,userDao),postId,postDao);
            p.setPost(getPost(postId,postDao));

            String message = getStringFieldValue(node,"message");
            p.setMessage(message);

            checkDoesSuchApplicationExist(userId,postId,postApplicationDao);
            p.setStatus(ApplicationStatus.ADDED);
            return p;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
