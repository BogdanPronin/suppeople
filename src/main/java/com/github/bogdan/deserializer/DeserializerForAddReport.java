package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Report;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.DeserializerService.*;
import static com.github.bogdan.service.UserService.checkDoesSuchUserExist;
import static com.github.bogdan.service.UserService.getUserById;

public class DeserializerForAddReport extends StdDeserializer<Report> {

    private User user;

    public DeserializerForAddReport(User user) {
        super(Report.class);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Report deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        try {
            Report report = new Report();

            report.setUser(user);

            String message = getStringFieldValue(node,"message");
            report.setMessage(message);

            String image = getOldStringFieldValue(node,"image",null);
            report.setImage(image);

            int user = getIntFieldValue(node,"reportedUser");
            checkDoesSuchUserExist(user);
            if(getUser().getId() == user){
                throw new WebException("Вы не можете пожаловаться на самого себя",400);
            }
            report.setReportedUser(getUserById(user));

            return report;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
