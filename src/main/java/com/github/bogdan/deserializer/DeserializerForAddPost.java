package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.User;
import com.github.bogdan.service.CategoryService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import static com.github.bogdan.service.CategoryService.getCategory;
import static com.github.bogdan.service.CityService.checkDoesCityExist;
import static com.github.bogdan.service.CityService.getCity;
import static com.github.bogdan.service.DeserializerService.*;

public class DeserializerForAddPost extends StdDeserializer<Post> {
    public DeserializerForAddPost(User user) {
        super(Post.class);
        this.user = user;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Post deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        try {
            Dao<Post,Integer> postDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Post.class);
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            Post post = new Post();

            post.setUser(user);

            int category = getIntFieldValue(node,"category");
            CategoryService.checkDoesSuchCategoryExist(category);
            post.setCategory(getCategory(category));

            String message = getStringFieldValue(node,"message");
            post.setMessage(message);

            int city = getIntFieldValue(node,"city");
            checkDoesCityExist(city);
            post.setCity(getCity(city));

            String image = getOldStringFieldValue(node,"image",null);
            post.setImage(image);

            LocalDate localDate = LocalDate.now();
            post.setDateOfCreate(localDate.toString());

            return post;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
