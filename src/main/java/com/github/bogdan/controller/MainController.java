package com.github.bogdan.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.deserializer.*;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.*;
import com.github.bogdan.serializer.*;
import com.github.bogdan.service.CategoryService;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import static com.github.bogdan.service.AuthService.checkAuthorization;
import static com.github.bogdan.service.CtxService.*;
import static com.github.bogdan.service.PaginationService.getPages;
import static com.github.bogdan.service.PaginationService.getPagination;
import static com.github.bogdan.service.PostApplicationService.checkDoesSuchApplicationExist;
import static com.github.bogdan.service.PostApplicationService.checkIsItUsersApplication;
import static com.github.bogdan.service.PostService.getPostUser;
import static com.github.bogdan.service.SortingService.getByQueryParams;
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
            simpleModule.addDeserializer(Post.class, new DeserializerForAddPost(getUserByLogin(ctx.basicAuthCredentials().getUsername())));
        }else if(clazz == Category.class){
            checkIsUserAdmin(ctx);
            simpleModule.addDeserializer(Category.class,new DeserializerForCategory());
        }else if(clazz == PostApplication.class){
            simpleModule.addDeserializer(PostApplication.class, new DeserializerForAddPostApplication(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getId()));
        }else if(clazz == Favorites.class){
            simpleModule.addDeserializer(Favorites.class, new DeserializerForAddFavorites(getUserByLogin(ctx.basicAuthCredentials().getUsername())));
        }else if(clazz == Report.class){
            simpleModule.addDeserializer(Report.class, new DeserializerForAddReport(getUserByLogin(ctx.basicAuthCredentials().getUsername())));
        }

        checkBodyRequestIsEmpty(ctx);
        String body = ctx.body();
        objectMapper.registerModule(simpleModule);
        Object obj = objectMapper.readValue(body, clazz);
        dao.create((T) obj);

        created(ctx);
    }

    public static <T> void get(Context ctx, Dao<T,Integer> dao,Class<T> clazz) throws JsonProcessingException, SQLException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException {



        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int userId = 0;
        if(ctx.basicAuthCredentialsExist()){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername())!=null) {
                userId = getUserByLogin(ctx.basicAuthCredentials().getUsername()).getId();
            }
        }
        simpleModule.addSerializer(User.class, new UserGetSerializer(userId));
        simpleModule.addSerializer(Post.class, new PostGetSerializer());
        simpleModule.addSerializer(Category.class, new CategoryGetSerializer());

        objectMapper.registerModule(simpleModule);
        int page = getPage(ctx);
        int size = getPagesSize(ctx);

        ArrayList<String> params = new ArrayList<>();
        if(clazz == User.class){
            User u = new User();
            params.addAll(u.getQueryParams());
        }else if(clazz == Post.class){
            Post p = new Post();
            params.addAll(p.getQueryParams());
            simpleModule.addSerializer(PostApplication.class, new PostApplicationGetSerializer());
        }else if(clazz == PostApplication.class){
            PostApplication p = new PostApplication();
            params.addAll(p.getQueryParams());
            simpleModule.addSerializer(PostApplication.class, new PostApplicationSerializer());
        }else if(clazz == Favorites.class){
            checkDoesBasicAuthEmpty(ctx);
            checkAuthorization(ctx);
            Favorites f = new Favorites();
            params.addAll(f.getQueryParams());
            simpleModule.addSerializer(PostApplication.class, new PostApplicationGetSerializer());
        }

        String serialized;
        if(doesQueryParamsEmpty(ctx,params)){
            serialized = objectMapper.writeValueAsString(getPagination(dao,page,size));
        }else serialized = objectMapper.writeValueAsString(getByQueryParams(dao,clazz,params,ctx));
        ctx.header("total-pages", String.valueOf(getPages(dao,getByQueryParams(dao,clazz,params,ctx),size)));

        ctx.result(serialized);
    }

    public static <T> void getById(Context ctx, Dao<T,Integer> dao,Class<T> clazz) throws SQLException, JsonProcessingException {
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int userId = 0;
        if(ctx.basicAuthCredentialsExist()){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername())!=null) {
                userId = getUserByLogin(ctx.basicAuthCredentials().getUsername()).getId();
            }
        }
        simpleModule.addSerializer(User.class, new UserGetSerializer(userId));
        simpleModule.addSerializer(Post.class, new PostGetSerializer());
        simpleModule.addSerializer(Category.class, new CategoryGetSerializer());

        objectMapper.registerModule(simpleModule);
        int id = Integer.parseInt(ctx.pathParam("id"));
        if(dao.queryForId(id) == null){
            throw new WebException("Такого не существует",404);
        }
        String serialized = objectMapper.writeValueAsString(dao.queryForId(id));

        ctx.result(serialized);

    }
    public static void getAuthorized(Context ctx) throws JsonProcessingException, SQLException {
        checkDoesBasicAuthEmpty(ctx);
        checkAuthorization(ctx);
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
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()!= Role.ADMIN){
                if(id != getUserByLogin(ctx.basicAuthCredentials().getUsername()).getId()){
                    youAreNotAdmin(ctx);
                }
            }
            checkDoesSuchUserExist(id);
            simpleModule.addDeserializer(User.class, new DeserializerForChangeUser(id));
        }else if(clazz == Category.class){
            simpleModule.addDeserializer(Category.class, new DeserializerForCategory(id));
            CategoryService.checkDoesSuchCategoryExist(id);
        } if(clazz == Post.class){
            simpleModule.addDeserializer(Post.class,new DeserializerForChangePost(id, getUserByLogin(ctx.basicAuthCredentials().getUsername()).getId()));
        }else if(clazz == PostApplication.class){
            simpleModule.addDeserializer(PostApplication.class,new DeserializerForChangePostApplication(id,getUserByLogin(ctx.basicAuthCredentials().getUsername())));
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()!= Role.ADMIN){
                checkIsItUsersApplication(id, getUserByLogin(ctx.basicAuthCredentials().getUsername()).getId());
            }
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
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()!= Role.ADMIN){
                if(id != getUserByLogin(ctx.basicAuthCredentials().getUsername()).getId()){
                    youAreNotAdmin(ctx);
                }
            }
            checkDoesSuchUserExist(id);
        }else if(clazz == Post.class){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()!= Role.ADMIN){
                if(id != getPostUser(id).getId()){
                    youAreNotAdmin(ctx);
                }
            }
        }else if(clazz == Category.class){
            checkIsUserAdmin(getUserByLogin(ctx.basicAuthCredentials().getUsername()));
            CategoryService.checkDoesSuchCategoryExist(id);
        }else if(clazz == PostApplication.class){
            if(getUserByLogin(ctx.basicAuthCredentials().getUsername()).getRole()!= Role.ADMIN){
                checkIsItUsersApplication(id, getUserByLogin(ctx.basicAuthCredentials().getUsername()).getId());
            }
            checkDoesSuchApplicationExist(id);
        }

        objectMapper.registerModule(simpleModule);
        Object obj = dao.queryForId(id);
        dao.delete((T) obj);
        deleted(ctx);
    }
}
