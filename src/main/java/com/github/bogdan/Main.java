package com.github.bogdan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.controller.MainController;
import com.github.bogdan.controller.UserController;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.User;
import com.github.bogdan.serializer.WebExceptionSerializer;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        Javalin app = Javalin
                .create();
        app.config = new JavalinConfig().enableDevLogging();
        app.start(22867);
        Dao<User, Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, User.class);
        app.post("/users", ctx -> MainController.add(ctx,userDao,User.class));
        app.get("/users", ctx -> MainController.get(ctx,userDao, User.class));
        app.patch("/users/:id", ctx -> MainController.change(ctx,userDao,User.class));

        app.exception(WebException.class, (e, ctx) -> {
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(WebException.class,new WebExceptionSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);
            try {
                ctx.result(objectMapper.writeValueAsString(e));
                ctx.status(e.getStatus());
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
        });

    }
}
