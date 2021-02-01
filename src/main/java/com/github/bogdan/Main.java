package com.github.bogdan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.controller.MainController;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.*;
import com.github.bogdan.serializer.WebExceptionSerializer;
import com.github.bogdan.service.SortingService;
import com.github.bogdan.utilitis.NewThread;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.Javalin;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {


        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.enableDevLogging();
            javalinConfig.enableCorsForAllOrigins();
            javalinConfig.defaultContentType = "application/json";
        }).start(22867);


        if (args.length == 0) {
            throw new RuntimeException("Args can't be null");
        }

        String jdbcConnectionString = "jdbc:sqlite:" + args[0];
        DatabasePath.setPath(jdbcConnectionString);
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(jdbcConnectionString);

        Dao<User, Integer> userDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), User.class);
        Dao<Category,Integer> categoryDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), Category.class);
        Dao<Post,Integer> postDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), Post.class);
        Dao<PostApplication,Integer> postApplicationDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(),PostApplication.class);
        Dao<Favorites, Integer> favoritesDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(), Favorites.class);
        Dao<Report, Integer> reportDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(),Report.class);
        Dao<Cities, Integer> citiesDao = DaoManager.createDao(databaseConfiguration.getConnectionSource(),Cities.class);
        NewThread thread = new NewThread(userDao,postDao,postApplicationDao);
        thread.start();

        app.post("/users", ctx -> MainController.add(ctx,userDao,User.class));
        app.get("/users", ctx -> MainController.get(ctx,userDao, User.class));
        app.get("/users/:id", ctx -> MainController.getById(ctx,userDao, User.class));
        app.patch("/users/:id", ctx -> MainController.change(ctx,userDao,User.class));
        app.delete("/users/:id",ctx -> MainController.delete(ctx,userDao,User.class));

        app.post("/category",ctx -> MainController.add(ctx,categoryDao, Category.class));
        app.get("/category", ctx -> MainController.get(ctx,categoryDao, Category.class));
        app.patch("/category/:id",ctx -> MainController.change(ctx,categoryDao, Category.class));
        app.delete("/category/:id", ctx -> MainController.delete(ctx,categoryDao, Category.class));

        app.post("/post", ctx -> MainController.add(ctx,postDao,Post.class));
        app.get("/post", ctx -> MainController.get(ctx,postDao,Post.class));
        app.patch("/post/:id", ctx -> MainController.change(ctx,postDao,Post.class));
        app.delete("/post/:id", ctx -> MainController.delete(ctx,postDao,Post.class));
        //app.get("/postSort",ctx -> SortingService.sort(postDao,ctx));

        app.post("/postApplication", ctx -> MainController.add(ctx,postApplicationDao,PostApplication.class));
        app.patch("/postApplication/:id", ctx -> MainController.change(ctx,postApplicationDao,PostApplication.class));
        app.get("/postApplication", ctx -> MainController.get(ctx,postApplicationDao,PostApplication.class));
        app.delete("/postApplication", ctx -> MainController.get(ctx,postApplicationDao,PostApplication.class));
        app.get("/userApplication",ctx -> MainController.getUserApplications(ctx));
        app.post("/favorites", ctx -> MainController.add(ctx,favoritesDao,Favorites.class));
        //app.patch("/postApplication/:id", ctx -> MainController.change(ctx,postApplicationDao,PostApplication.class));
        app.get("/favorites", ctx -> MainController.get(ctx,favoritesDao,Favorites.class));
//        app.delete("/postApplication", ctx -> MainController.get(ctx,postApplicationDao,PostApplication.class));

        app.post("/report", ctx -> MainController.add(ctx,reportDao, Report.class));

        app.get("/cities", ctx -> MainController.get(ctx,citiesDao, Cities.class));

        app.get("/authorized",ctx -> MainController.getAuthorized(ctx));

        app.get("/search",ctx -> MainController.search(ctx,userDao));

        app.exception(IllegalArgumentException.class,(e, ctx) ->{
            WebException w = new WebException("Such enum constant doesn't exist: "+e.getMessage(),400);
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(WebException.class,new WebExceptionSerializer());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(simpleModule);
            try {
                ctx.result(objectMapper.writeValueAsString(w));
                ctx.status(w.getStatus());
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
        });
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
