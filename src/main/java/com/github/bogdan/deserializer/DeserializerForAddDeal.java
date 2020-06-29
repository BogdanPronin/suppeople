package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.github.bogdan.service.DeserializerService.getIntFieldValue;
import static com.github.bogdan.service.PostApplicationService.checkDoesSuchApplicationExist;
import static com.github.bogdan.service.UserService.checkBooleanIsUserAdmin;
import static com.github.bogdan.service.UserService.checkDoesSuchUserExist;

public class DeserializerForAddDeal extends StdDeserializer<Deal> {
    public DeserializerForAddDeal(User user) {
        super(Deal.class);
        this.user = user;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Deal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            Dao<PostApplication,Integer> postApplicationDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,PostApplication.class);
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Deal deal = new Deal();

            int postApplicationId = getIntFieldValue(node,"postApplicationId");
            checkDoesSuchApplicationExist(postApplicationId);
            if(user.getRole() != Role.ADMIN){
                if(user.getId() != postApplicationDao.queryForId(postApplicationId).getPost().getUser().getId() ){
                    throw new WebException("You aren't admin",400);
                }
            }
            deal.setPostApplication(postApplicationDao.queryForId(postApplicationId));

            LocalDate localDate = LocalDate.now();
            deal.setStartDate(localDate.toString());

            deal.setStatus(Status.PROCESSING);
            return deal;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
