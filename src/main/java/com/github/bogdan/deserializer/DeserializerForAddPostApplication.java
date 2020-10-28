package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.PostApplication;
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
import static com.github.bogdan.service.UserService.getUserById;

public class DeserializerForAddPostApplication extends StdDeserializer<PostApplication> {
    public DeserializerForAddPostApplication(int userId) {
        super(PostApplication.class);
        this.userId = userId;
    }

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
            Dao<PostApplication,Integer> postApplicationDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,PostApplication.class);
            PostApplication p = new PostApplication();

            p.setUser(UserService.getUserById(userId));

            int postId = getIntFieldValue(node,"postId");
            checkDoesSuchPostExist(postId);
            checkItIsNotUserPost(UserService.getUserById(userId),postId);
            p.setPost(getPost(postId));

            String message = getStringFieldValue(node,"message");
            p.setMessage(message);

            checkDoesSuchApplicationExist(userId,postId);
            return p;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
