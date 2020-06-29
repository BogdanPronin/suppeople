package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.Deadline;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.UserArea;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.github.bogdan.service.AreaOfActivityService.checkDoesSuchAreaOfActivityExist;
import static com.github.bogdan.service.AreaOfActivityService.getAreaOfActivity;
import static com.github.bogdan.service.DeserializerService.*;
import static com.github.bogdan.service.LocalDateService.addDeadlineToBase;
import static com.github.bogdan.service.LocalDateService.checkDeadline;
import static com.github.bogdan.service.UserService.*;

public class DeserializerForAddUserArea extends StdDeserializer<UserArea> {
    public DeserializerForAddUserArea(int id) {
        super(UserArea.class);
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
    public UserArea deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            Dao<UserArea,Integer> userAreaDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,UserArea.class);
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            UserArea userArea = new UserArea();

            int userId = getIntFieldValue(node,"userId");
            if(userId != id){
                checkIsUserAdmin(id);
            }
            checkDoesSuchUserExist(userId);
            userArea.setUser(getUser(userId));

            int areaOfActivity = getIntFieldValue(node, "areaOfActivity");
            checkDoesSuchAreaOfActivityExist(areaOfActivity);
            userArea.setAreaOfActivity(getAreaOfActivity(areaOfActivity));

            String exp = getOldStringFieldValue(node,"experience",null);
            userArea.setExperience(exp);

            return userArea;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
