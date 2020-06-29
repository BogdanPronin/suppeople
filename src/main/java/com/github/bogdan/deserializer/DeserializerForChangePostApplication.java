package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.controller.MainController;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.PostApplication;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.DeserializerService.*;
import static com.github.bogdan.service.PostApplicationService.checkDoesSuchApplicationExist;
import static com.github.bogdan.service.PostService.checkDoesSuchPostExist;
import static com.github.bogdan.service.PostService.getPost;
import static com.github.bogdan.service.UserService.checkBooleanIsUserAdmin;

public class DeserializerForChangePostApplication extends StdDeserializer<PostApplication> {
    public DeserializerForChangePostApplication(int id) {
        super(PostApplication.class);
        this.id = id;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public PostApplication deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Logger LOGGER = LoggerFactory.getLogger(DeserializerForChangePostApplication.class);

        try {
            Dao<PostApplication,Integer> postApplicationDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,PostApplication.class);
            PostApplication p = new PostApplication();
            checkDoesSuchApplicationExist(id);
            PostApplication postApplicationBase = postApplicationDao.queryForId(id);

            p.setId(id);

            String message = getOldStringFieldValue(node,"message",postApplicationBase.getMessage());
            p.setMessage(message);
            int postId;
            if(checkBooleanIsUserAdmin(postApplicationBase.getUser().getId())){
                postId = getOldIntFieldValue(node,"postId",postApplicationBase.getPost().getId());
                checkDoesSuchPostExist(postId);
            }else postId = postApplicationBase.getPost().getId();

            p.setPost(getPost(postId));

            p.setUser(postApplicationBase.getUser());

            return p;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

            return null;
    }
}
