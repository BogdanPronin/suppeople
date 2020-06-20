package com.github.bogdan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.controller.MainController;
import com.github.bogdan.controller.UserController;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.AreaOfActivity;
import com.github.bogdan.model.Deadline;
import com.github.bogdan.model.Post;
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
        Dao<AreaOfActivity,Integer> areaOfActivityDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, AreaOfActivity.class);
        Dao<Post,Integer> postDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Post.class);
        Dao<Deadline,Integer> deadlineDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Deadline.class);

        app.post("/users", ctx -> MainController.add(ctx,userDao,User.class));
        app.get("/users", ctx -> MainController.get(ctx,userDao, User.class));
        app.patch("/users/:id", ctx -> MainController.change(ctx,userDao,User.class));
        app.delete("/users/:id",ctx -> MainController.delete(ctx,userDao,User.class));

        app.post("/areaOfActivity",ctx -> MainController.add(ctx,areaOfActivityDao,AreaOfActivity.class));
        app.get("/areaOfActivity", ctx -> MainController.get(ctx,areaOfActivityDao, AreaOfActivity.class));
        app.patch("/areaOfActivity/:id",ctx -> MainController.change(ctx,areaOfActivityDao,AreaOfActivity.class));
        app.delete("/areaOfActivity/:id", ctx -> MainController.delete(ctx,areaOfActivityDao, AreaOfActivity.class));

        app.post("/post", ctx -> MainController.add(ctx,postDao,Post.class));
        app.get("/post", ctx -> MainController.get(ctx,postDao,Post.class));
        app.patch("/post/:id", ctx -> MainController.change(ctx,postDao,Post.class));
        app.delete("/post/:id", ctx -> MainController.delete(ctx,postDao,Post.class));

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
