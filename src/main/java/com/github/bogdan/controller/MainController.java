package com.github.bogdan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.deserializer.*;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.*;
import com.github.bogdan.serializer.*;
import com.github.bogdan.service.CategoryService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    private static DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(DatabasePath.getPath());

    private static Dao<User, Integer> userDao;
    private static Dao<Category, Integer> categoryDao;
    private static Dao<Post, Integer> postDao;
    private static Dao<PostApplication, Integer> postApplicationDao;
    private static Dao<Favorites, Integer> favoritesDao;
    private static Dao<Report, Integer> reportDao;
    private static Dao<Cities, Integer> citiesDao;

    static {
        try {
            citiesDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), Cities.class);
            reportDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), Report.class);
            favoritesDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), Favorites.class);
            postApplicationDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), PostApplication.class);
            postDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), Post.class);
            categoryDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), Category.class);
            userDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), User.class);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static <T> void add(Context ctx, Dao<T, Integer> dao, Class<T> clazz) throws JsonProcessingException, SQLException {
        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();

        if (clazz == User.class) {
            simpleModule.addDeserializer(User.class, new DeserializerForAddUser(userDao, citiesDao));
        } else {
            checkDoesBasicAuthEmpty(ctx);
            checkAuthorization(ctx, userDao);
        }

        if (clazz == Post.class) {
            simpleModule.addDeserializer(Post.class, new DeserializerForAddPost(getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao), categoryDao, citiesDao));
        } else if (clazz == Category.class) {
            checkIsUserAdmin(ctx, userDao);
            simpleModule.addDeserializer(Category.class, new DeserializerForCategory(categoryDao));
        } else if (clazz == PostApplication.class) {
            simpleModule.addDeserializer(PostApplication.class, new DeserializerForAddPostApplication(getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId(), userDao, postDao, postApplicationDao));
        } else if (clazz == Favorites.class) {
            simpleModule.addDeserializer(Favorites.class, new DeserializerForAddFavorites(getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao), postDao, favoritesDao));
        } else if (clazz == Report.class) {
            simpleModule.addDeserializer(Report.class, new DeserializerForAddReport(getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao), userDao));
        }

        checkBodyRequestIsEmpty(ctx);
        String body = ctx.body();
        objectMapper.registerModule(simpleModule);
        Object obj = objectMapper.readValue(body, clazz);
        dao.create((T) obj);
        if (clazz != User.class) {
            created(ctx);
        } else {
            simpleModule.addSerializer(User.class, new UserGetSerializer(((User) obj).getId()));
            ctx.result(objectMapper.writeValueAsString(obj));
        }
    }

    public static <T> void get(Context ctx, Dao<T, Integer> dao, Class<T> clazz) throws JsonProcessingException, SQLException, NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException {
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int userId = 0;
        if (ctx.basicAuthCredentialsExist()) {
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao) != null) {
                userId = getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId();
            }
        }
        simpleModule.addSerializer(User.class, new UserGetSerializer(userId));
        simpleModule.addSerializer(Post.class, new PostGetSerializer(postApplicationDao));
        simpleModule.addSerializer(Category.class, new CategoryGetSerializer());

        objectMapper.registerModule(simpleModule);
        int page = getPage(ctx);
        int size = getPagesSize(ctx);

        ArrayList<String> params = new ArrayList<>();
        if (clazz == User.class) {
            User u = new User();
            params.addAll(u.getQueryParams());
        } else if (clazz == Post.class) {
            Post p = new Post();
            params.addAll(p.getQueryParams());
            simpleModule.addSerializer(PostApplication.class, new PostApplicationGetSerializer());
        } else if (clazz == PostApplication.class) {
            PostApplication p = new PostApplication();
            params.addAll(p.getQueryParams());

            simpleModule.addSerializer(PostApplication.class, new PostApplicationSerializer());
        } else if (clazz == Favorites.class) {
            checkDoesBasicAuthEmpty(ctx);
            checkAuthorization(ctx, userDao);
            Favorites f = new Favorites();
            params.addAll(f.getQueryParams());
            simpleModule.addSerializer(PostApplication.class, new PostApplicationGetSerializer());
        }

        String serialized;

        LOGGER.info(String.valueOf(getPagination(getByQueryParams(userId, userDao, postDao, postApplicationDao, dao, clazz, params, ctx), page, size)));
        serialized = objectMapper.writeValueAsString(getPagination(getByQueryParams(userId, userDao, postDao, postApplicationDao, dao, clazz, params, ctx), page, size));

        ctx.header("total-pages", String.valueOf(getPages(dao, getByQueryParams(userId, userDao, postDao, postApplicationDao, dao, clazz, params, ctx), size)));

        ctx.result(serialized);
    }

    public static <T> void getById(Context ctx, Dao<T, Integer> dao, Class<T> clazz) throws SQLException, JsonProcessingException {
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int userId = 0;
        if (ctx.basicAuthCredentialsExist()) {
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao) != null) {
                userId = getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId();
            }
        }
        simpleModule.addSerializer(User.class, new UserGetSerializer(userId));
        simpleModule.addSerializer(Post.class, new PostGetSerializer(postApplicationDao));
        simpleModule.addSerializer(Category.class, new CategoryGetSerializer());

        objectMapper.registerModule(simpleModule);
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (dao.queryForId(id) == null) {
            throw new WebException("Такого не существует", 404);
        }
        String serialized = objectMapper.writeValueAsString(dao.queryForId(id));

        ctx.result(serialized);

    }

    public static <T> void change(Context ctx, Dao<T, Integer> dao, Class<T> clazz) throws JsonProcessingException, SQLException {
        checkDoesBasicAuthEmpty(ctx);

        ctx.header("content-type", "app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("id"));
        checkBodyRequestIsEmpty(ctx);
        String body = ctx.body();

        checkAuthorization(ctx, userDao);
        if (clazz == User.class) {
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getRole() != Role.ADMIN) {
                if (id != getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId()) {
                    youAreNotAdmin(ctx);
                }
            }
            checkDoesSuchUserExist(id, userDao);
            simpleModule.addDeserializer(User.class, new DeserializerForChangeUser(id, userDao, citiesDao));
        } else if (clazz == Category.class) {
            simpleModule.addDeserializer(Category.class, new DeserializerForCategory(id));
            CategoryService.checkDoesSuchCategoryExist(id, categoryDao);
        }
        if (clazz == Post.class) {
            simpleModule.addDeserializer(Post.class, new DeserializerForChangePost(id, getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId(), postDao, userDao, postApplicationDao));
        } else if (clazz == PostApplication.class) {
            simpleModule.addDeserializer(PostApplication.class, new DeserializerForChangePostApplication(id, getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao), postApplicationDao, userDao, postDao));
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getRole() != Role.ADMIN) {
                checkIsItUsersApplication(id, getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId(), postApplicationDao);
            }
        }

        objectMapper.registerModule(simpleModule);
        Object obj = objectMapper.readValue(body, clazz);

        dao.update((T) obj);
        updated(ctx);
    }

    public static <T> void delete(Context ctx, Dao<T, Integer> dao, Class<T> clazz) throws JsonProcessingException, SQLException {
        checkDoesBasicAuthEmpty(ctx);
        ctx.header("content-type:app/json");
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("id"));
        checkAuthorization(ctx, userDao);
        if (clazz == User.class) {
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getRole() != Role.ADMIN) {
                if (id != getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId()) {
                    youAreNotAdmin(ctx);
                }
            }
            checkDoesSuchUserExist(id, userDao);
        } else if (clazz == Post.class) {
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getRole() != Role.ADMIN) {
                if (id != getPostUser(id, postDao).getId()) {
                    youAreNotAdmin(ctx);
                }
            }
        } else if (clazz == Category.class) {
            checkIsUserAdmin(getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao));
            CategoryService.checkDoesSuchCategoryExist(id, categoryDao);
        } else if (clazz == PostApplication.class) {
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getRole() != Role.ADMIN) {
                checkIsItUsersApplication(id, getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId(), postApplicationDao);
            }
            checkDoesSuchApplicationExist(id, postApplicationDao);
        }

        objectMapper.registerModule(simpleModule);
        Object obj = dao.queryForId(id);
        dao.delete((T) obj);
        deleted(ctx);
    }

    public static void search(Context ctx, Dao<User, Integer> userDao) throws SQLException, JsonProcessingException {
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        String searchString = ctx.queryParam("searchString");
        Set<User> users = new HashSet<>();

        String[] search = searchString.toLowerCase().split(" ");
        if (search.length != 0) {
            for (User u : userDao.queryForAll()) {
                if (search.length == 1) {
                    if (u.getLname().toLowerCase().contains(search[0])
                            || u.getFname().toLowerCase().contains(search[0])
                    ) {
                        users.add(u);
                    }
                } else {
                    if (u.getFname().toLowerCase().contains(search[0])
                            && u.getLname().toLowerCase().contains(search[1])
                            || u.getFname().toLowerCase().contains(search[1])
                            && u.getLname().toLowerCase().contains(search[0])
                    ) {
                        users.add(u);
                    }
                }
            }
        }


        simpleModule.addSerializer(User.class, new UserGetSerializer(0));
        objectMapper.registerModule(simpleModule);
        ctx.result(objectMapper.writeValueAsString(users));
    }

    public static void getAuthorized(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();

        checkDoesBasicAuthEmpty(ctx);
        checkAuthorization(ctx, userDao);
        SimpleModule simpleModule = new SimpleModule();

        simpleModule.addSerializer(User.class, new UserGetSerializer(getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId()));
        objectMapper.registerModule(simpleModule);

        ctx.result(objectMapper.writeValueAsString(getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao)));
    }

    public static void getUserApplications(Context ctx) throws SQLException, JsonProcessingException {
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
//        simpleModule.addSerializer(Post.class, new PostGetSerializer(postApplicationDao));
//        simpleModule.addSerializer(Category.class, new CategoryGetSerializer());
        simpleModule.addSerializer(PostApplication.class, new PostApplicationSerializer());

        int page = getPage(ctx);
        int size = getPagesSize(ctx);
        ArrayList<PostApplication> postApplicationArrayList = new ArrayList<>();
        int userId = 0;
        if (ctx.basicAuthCredentialsExist()) {
            if (getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao) != null) {
                userId = getUserByLogin(ctx.basicAuthCredentials().getUsername(), userDao).getId();
            }
        }
        simpleModule.addSerializer(User.class, new UserGetSerializer(userId));
        objectMapper.registerModule(simpleModule);

        if (ctx.queryParam("user") != null) {
            checkIsUserAdmin(ctx, userDao);
            if (userDao.queryForId(Integer.parseInt(ctx.queryParam("user"))) != null) {
                userId = Integer.parseInt(ctx.queryParam("user"));
                for (PostApplication postApplication : postApplicationDao.queryForAll()) {
                    if (postApplication.getUser().getId() == userId) {
                        postApplicationArrayList.add(postApplication);
                    }
                }
            }
        } else {
            for (PostApplication postApplication : postApplicationDao.queryForAll()) {
                if (postApplication.getUser().getId() == userId) {
                    postApplicationArrayList.add(postApplication);
                }
            }
        }
        String serialized;
        serialized = objectMapper.writeValueAsString(getPagination(postApplicationArrayList, page, size));

        ctx.header("total-pages", String.valueOf(getPages(postApplicationDao, postApplicationArrayList, size)));

        ctx.result(serialized);

    }
}
