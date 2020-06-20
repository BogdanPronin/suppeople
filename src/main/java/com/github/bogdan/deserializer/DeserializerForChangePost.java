package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.Deadline;
import com.github.bogdan.model.Post;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.AreaOfActivityService.checkDoesSuchAreaOfActivityExist;
import static com.github.bogdan.service.AreaOfActivityService.getAreaOfActivity;
import static com.github.bogdan.service.DeserializerService.*;
import static com.github.bogdan.service.LocalDateService.addDeadlineToBase;
import static com.github.bogdan.service.LocalDateService.checkDeadline;
import static com.github.bogdan.service.PostService.checkDoesSuchPostExist;
import static com.github.bogdan.service.PostService.checkPostUser;
import static com.github.bogdan.service.UserService.getUser;

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

            p.setUser(getUser(userId));

            String task = getOldStringFieldValue(node,"task",postBase.getTask());
            p.setTask(task);

            if(checkNullFieldValue(node,"areaOfActivity")){
                p.setAreaOfActivity(postBase.getAreaOfActivity());
            }else {
                checkDoesSuchAreaOfActivityExist(node.get("areaOfActivity").asInt());
                p.setAreaOfActivity(getAreaOfActivity(node.get("areaOfActivity").asInt()));
            }

            if(checkNullFieldValue(node,"deadline")){
                p.setDeadline(postBase.getDeadline());
            }else {

                String s =node.get("deadline").asText();
                Deadline deadline = checkDeadline(s);
                addDeadlineToBase(deadline);
                p.setDeadline(deadline);
            }

            p.setDateOfCreate(postBase.getDateOfCreate());
            return p;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
