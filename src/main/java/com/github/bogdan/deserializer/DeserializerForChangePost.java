package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.Status;
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
    public DeserializerForChangePost(int postId, int userId) {
        super(Post.class);
        this.postId = postId;
        this.userId = userId;
    }

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
            Dao<Post,Integer> postDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Post.class);
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Post p = new Post();

            p.setId(postId);

            Post postBase = postDao.queryForId(postId);

            checkDoesSuchPostExist(postId);

            checkPostUser(postId,userId);

            p.setUser(postBase.getUser());


            p.setDateOfCreate(postBase.getDateOfCreate());

            String  status = getOldStringFieldValue(node,"status",postBase.getStatus().toString());
            if(status.equals(Status.COMPLETED.toString()) &&!getPostApplications(p.getId()).isEmpty()){
                addPostQt(p);
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
