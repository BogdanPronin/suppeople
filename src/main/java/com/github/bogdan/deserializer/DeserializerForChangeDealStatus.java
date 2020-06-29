package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.controller.MainController;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.Deal;
import com.github.bogdan.model.Role;
import com.github.bogdan.model.Status;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.github.bogdan.service.AreaOfActivityService.checkDoesSuchAreaOfActivityExist;
import static com.github.bogdan.service.DealService.checkDoesSuchDealExist;
import static com.github.bogdan.service.DeserializerService.*;
import static com.github.bogdan.service.PostApplicationService.*;

public class DeserializerForChangeDealStatus extends StdDeserializer<Deal> {
    public DeserializerForChangeDealStatus(int dealId, User user) {
        super(Deal.class);
        this.dealId = dealId;
        this.user = user;
    }

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    private int dealId;

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
            Logger LOGGER = LoggerFactory.getLogger(DeserializerForChangeDealStatus.class);

            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Dao<Deal,Integer> dealDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Deal.class);
            checkDoesSuchDealExist(dealId);

            Deal dealBase = dealDao.queryForId(dealId);
            Deal d = new Deal();
            d.setId(dealId);

            if(user.getRole() == Role.ADMIN){
                if(checkNullFieldValue(node,"postApplication")){
                    d.setPostApplication(dealBase.getPostApplication());
                }else {
                    checkDoesSuchApplicationExist(node.get("postApplication").asInt());
                    d.setPostApplication(getPostApplication(node.get("postApplication").asInt()));
                }
            }else d.setPostApplication(dealBase.getPostApplication());

            d.setStartDate(dealBase.getStartDate());

            String status = getOldStringFieldValue(node,"status",dealBase.getStatus().toString());
            d.setStatus(Status.valueOf(status));
            if(Status.valueOf(status) == Status.ENDED){
                d.setEndDate(LocalDate.now().toString());
            }else{
                d.setEndDate(null);
            }
            return d;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
