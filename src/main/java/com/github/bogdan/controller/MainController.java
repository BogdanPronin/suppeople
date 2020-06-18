package com.github.bogdan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.deserializer.DeserializerForAddPost;
import com.github.bogdan.deserializer.DeserializerForAddUser;
import com.github.bogdan.deserializer.DeserializerForAreaOfActivity;
import com.github.bogdan.deserializer.DeserializerForChangeUser;
import com.github.bogdan.model.AreaOfActivity;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.Role;
import com.github.bogdan.model.User;
import com.github.bogdan.serializer.UserGetSerializer;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import static com.github.bogdan.service.AuthService.checkAuthorization;
import static com.github.bogdan.service.CtxService.*;
import static com.github.bogdan.service.PaginationService.getPagination;
import static com.github.bogdan.service.PostService.getPostUser;
import static com.github.bogdan.service.UserService.*;

public class MainController {
    static Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    public static <T> void add(Context ctx, Dao<T,Integer> dao,Class<T> clazz) throws JsonProcessingException, SQLException {
        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();

        if (clazz == User.class) {
            simpleModule.addDeserializer(User.class, new DeserializerForAddUser());
        }else {
            checkDoesBasicAuthEmpty(ctx);
            checkAuthorization(ctx);
        }

        if(clazz == Post.class){
            simpleModule.addDeserializer(Post.class, new DeserializerForAddPost(getUser(ctx.basicAuthCredentials().getUsername())));
        }else if(clazz == AreaOfActivity.class){
            checkIsUserAdmin(ctx);
            simpleModule.addDeserializer(AreaOfActivity.class,new DeserializerForAreaOfActivity());
        }

        checkBodyRequestIsEmpty(ctx);
        String body = ctx.body();
        objectMapper.registerModule(simpleModule);
        Object obj = objectMapper.readValue(body, clazz);
        dao.create((T) obj);
        created(ctx);
    }

    public static <T> void get(Context ctx, Dao<T,Integer> dao,Class<T> clazz) throws JsonProcessingException, SQLException {

        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();

        if (clazz == User.class) {
            simpleModule.addSerializer(User.class, new UserGetSerializer());
        }

        objectMapper.registerModule(simpleModule);

        int page = getPage(ctx);
        int size = getPagesSize(ctx);
        String serialized = objectMapper.writeValueAsString(getPagination(dao,page,size));
        ctx.result(serialized);
    }

    public static <T> void change(Context ctx, Dao<T,Integer> dao,Class<T> clazz) throws JsonProcessingException, SQLException {
        checkDoesBasicAuthEmpty(ctx);

        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("id"));
        checkBodyRequestIsEmpty(ctx);
        String body = ctx.body();

        checkAuthorization(ctx);
        if (clazz == User.class) {
            if(getUser(ctx.basicAuthCredentials().getUsername()).getRole()!= Role.ADMIN){
                if(id != getUser(ctx.basicAuthCredentials().getUsername()).getId()){
                    youAreNotAdmin(ctx);
                }
            }
            checkDoesSuchUserExist(id);
            simpleModule.addDeserializer(User.class, new DeserializerForChangeUser(id));
        }
        objectMapper.registerModule(simpleModule);
        Object obj = objectMapper.readValue(body, clazz);
        dao.update((T) obj);
        updated(ctx);
    }

    public static <T> void delete(Context ctx, Dao<T,Integer> dao,Class<T> clazz) throws JsonProcessingException, SQLException {
        checkDoesBasicAuthEmpty(ctx);
        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("id"));
        checkAuthorization(ctx);
        if (clazz == User.class) {
            if(getUser(ctx.basicAuthCredentials().getUsername()).getRole()!= Role.ADMIN){
                if(id != getUser(ctx.basicAuthCredentials().getUsername()).getId()){
                    youAreNotAdmin(ctx);
                }
            }
            checkDoesSuchUserExist(id);
        }else if(clazz == Post.class){
            if(getUser(ctx.basicAuthCredentials().getUsername()).getRole()!= Role.ADMIN){
                if(id != getPostUser(id).getId()){
                    youAreNotAdmin(ctx);
                }
            }
        }
        objectMapper.registerModule(simpleModule);
        Object obj = dao.queryForId(id);
        dao.delete((T) obj);
        deleted(ctx);
    }
}
