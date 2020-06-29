package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.UserArea;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.AreaOfActivityService.checkDoesSuchAreaOfActivityExist;
import static com.github.bogdan.service.AreaOfActivityService.getAreaOfActivity;
import static com.github.bogdan.service.DeserializerService.*;
import static com.github.bogdan.service.UserService.*;

public class DeserializerForChangeUserArea extends StdDeserializer<UserArea> {
    public DeserializerForChangeUserArea(int userAreaId, int id){
        super(UserArea.class);
        this.userAreaId = userAreaId;
        this.id = id;
    }

    private int userAreaId;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserAreaId() {
        return userAreaId;
    }

    public void setUserAreaId(int userAreaId) {
        this.userAreaId = userAreaId;
    }

    @Override
    public UserArea deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            Dao<UserArea,Integer> userAreaDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserArea.class);
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            UserArea userAreaBase = userAreaDao.queryForId(userAreaId);
            UserArea userArea = new UserArea();
            userArea.setId(userAreaId);

            int userId = getOldIntFieldValue(node,"userId",userAreaBase.getId());
            if(userId != id){
                checkIsUserAdmin(id);
            }
            checkDoesSuchUserExist(userId);
            userArea.setUser(getUser(userId));

            int areaOfActivity = getOldIntFieldValue(node, "areaOfActivity",userAreaBase.getAreaOfActivity().getId());
            checkDoesSuchAreaOfActivityExist(areaOfActivity);
            userArea.setAreaOfActivity(getAreaOfActivity(areaOfActivity));

            String exp = getOldStringFieldValue(node,"experience",null);
            userArea.setExperience(exp);

            userArea.setTaskExecutionSpeed(0.0);
            return userArea;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
