package com.github.bogdan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.deserializer.DeserializerForAddUser;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;

import java.sql.SQLException;

import static com.github.bogdan.service.CtxService.*;

public class UserController {
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static void add(Context ctx, Dao<User,Integer> userDao) throws JsonProcessingException, SQLException {
        checkBodyRequestIsEmpty(ctx);
        checkDoesBasicAuthEmpty(ctx);
        ctx.header("content-type:app/json");
        String body = ctx.body();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(User.class, new DeserializerForAddUser());
        objectMapper.registerModule(simpleModule);

        User user = objectMapper.readValue(body, User.class);
        userDao.create(user);
        created(ctx);
    }
    public static void get(Context ctx,Dao<User, Integer> userDao){

    }
}
