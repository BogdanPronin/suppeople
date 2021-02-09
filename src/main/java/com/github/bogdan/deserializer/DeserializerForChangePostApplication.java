package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.model.ApplicationStatus;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.PostApplication;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.DeserializerService.*;
import static com.github.bogdan.service.PostApplicationService.checkDoesSuchApplicationExist;
import static com.github.bogdan.service.PostService.*;
import static com.github.bogdan.service.UserService.checkBooleanIsUserAdmin;

public class DeserializerForChangePostApplication extends StdDeserializer<PostApplication> {
    public DeserializerForChangePostApplication(int id, User user, Dao<PostApplication, Integer> postApplicationDao, Dao<User, Integer> userDao, Dao<Post, Integer> postDao) {
        super(PostApplication.class);
        this.id = id;
        this.user = user;
        this.postApplicationDao = postApplicationDao;
        this.userDao = userDao;
        this.postDao = postDao;
    }

    private User user;
    private int id;
    private Dao<User, Integer> userDao;
    private Dao<PostApplication, Integer> postApplicationDao;
    private Dao<Post, Integer> postDao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public PostApplication deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Logger LOGGER = LoggerFactory.getLogger(DeserializerForChangePostApplication.class);

        try {
            PostApplication p = new PostApplication();
            checkDoesSuchApplicationExist(id, postApplicationDao);
            PostApplication postApplicationBase = postApplicationDao.queryForId(id);

            p.setId(id);
            p.setMessage(postApplicationBase.getMessage());
            checkPostUser(postApplicationBase.getPost().getId(), getUser().getId(), postDao);
            String status = getOldStringFieldValue(node, "status", "ADDED");
            p.setStatus(ApplicationStatus.valueOf(status));


            int postId;
            if (checkBooleanIsUserAdmin(postApplicationBase.getUser().getId(), userDao)) {
                postId = getOldIntFieldValue(node, "postId", postApplicationBase.getPost().getId());
                checkDoesSuchPostExist(postId, postDao);
                String message = getOldStringFieldValue(node, "message", postApplicationBase.getMessage());
                p.setMessage(message);
            } else postId = postApplicationBase.getPost().getId();

            p.setPost(getPost(postId, postDao));

            p.setUser(postApplicationBase.getUser());

            return p;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
