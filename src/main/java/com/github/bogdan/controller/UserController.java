package com.github.bogdan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.deserializer.DeserializerForAddUser;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;

import java.sql.SQLException;

import static com.github.bogdan.service.СtxService.*;

public class UserController {
    public static void add(Context ctx, Dao<User,Integer> userDao) throws JsonProcessingException, SQLException {
        String body = ctx.body();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(User.class, new DeserializerForAddUser());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);
        User user = objectMapper.readValue(body, User.class);
        userDao.create(user);
        created(ctx);
    }
}
