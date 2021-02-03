package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.model.*;
import com.github.bogdan.service.CategoryService;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import static com.github.bogdan.service.CategoryService.getCategory;
import static com.github.bogdan.service.CityService.checkDoesCityExist;
import static com.github.bogdan.service.CityService.getCity;
import static com.github.bogdan.service.DeserializerService.*;

public class DeserializerForAddPost extends StdDeserializer<Post> {
    public DeserializerForAddPost(User user,Dao<Category,Integer> categoryDao,Dao<Cities, Integer> citiesDao) {
        super(Post.class);
        this.user = user;
        this.categoryDao = categoryDao;
        this.citiesDao = citiesDao;
    }
    private User user;
    private Dao<Category,Integer> categoryDao;
    private Dao<Cities, Integer> citiesDao;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Post deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            Post post = new Post();

            post.setUser(user);

            int category = getIntFieldValue(node,"category");
            CategoryService.checkDoesSuchCategoryExist(category,categoryDao);
            post.setCategory(getCategory(category,categoryDao));

            String message = getStringFieldValue(node,"message");
            post.setMessage(message);

            int city = getIntFieldValue(node,"city");
            checkDoesCityExist(city, citiesDao);
            post.setCity(getCity(city, citiesDao));

            String image = getOldStringFieldValue(node,"image",null);
            post.setImage(image);
            post.setStatus(Status.CREATED);
            LocalDate localDate = LocalDate.now();
            post.setDateOfCreate(localDate.toString());

            return post;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
