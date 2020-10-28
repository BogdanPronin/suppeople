package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Favorites;
import com.github.bogdan.model.Report;
import com.github.bogdan.model.User;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.DeserializerService.getIntFieldValue;
import static com.github.bogdan.service.PostService.checkDoesSuchPostExist;
import static com.github.bogdan.service.PostService.getPost;

public class DeserializerForAddFavorites  extends StdDeserializer<Favorites> {

    private User user;
    public DeserializerForAddFavorites(User user) {
        super(Favorites.class);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Favorites deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Favorites favorites = new Favorites();
            favorites.setUser(getUser());

            int post = getIntFieldValue(node,"post");
            checkDoesSuchPostExist(post);
            if(getPost(post).getUser().equals(user)){
                throw new WebException("Это ваш пост",400);
            }
            favorites.setPost(getPost(post));

            return favorites;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
