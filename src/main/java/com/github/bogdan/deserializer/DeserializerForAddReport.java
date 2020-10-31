package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Report;
import com.github.bogdan.model.User;
import com.github.bogdan.service.UserService;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.DeserializerService.*;
import static com.github.bogdan.service.UserService.checkDoesSuchUserExist;

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
            report.setReportedUser(UserService.getUserById(user));

            return report;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
