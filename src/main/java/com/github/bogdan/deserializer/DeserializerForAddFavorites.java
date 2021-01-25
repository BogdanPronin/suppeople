package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Favorites;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import static com.github.bogdan.service.DeserializerService.getIntFieldValue;
import static com.github.bogdan.service.FavoritesService.checkUniqueFavorite;
import static com.github.bogdan.service.PostService.checkDoesSuchPostExist;
import static com.github.bogdan.service.PostService.getPost;

public class DeserializerForAddFavorites  extends StdDeserializer<Favorites> {

    private User user;
    public DeserializerForAddFavorites(User user,Dao<Post, Integer> postDao,Dao<Favorites, Integer> favoritesDao) {
        super(Favorites.class);
        this.postDao = postDao;
        this.user = user;
        this.favoritesDao = favoritesDao;
    }
    private Dao<Post, Integer> postDao;
    private Dao<Favorites, Integer> favoritesDao;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    static Logger log = Logger.getLogger(DeserializerForAddFavorites.class.getName());

    @Override
    public Favorites deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
           JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Favorites favorites = new Favorites();
            favorites.setUser(getUser());

            int post = getIntFieldValue(node,"post");
            checkDoesSuchPostExist(post,postDao);
            if(getPost(post,postDao).getUser().equals(user)){
                throw new WebException("Это ваш пост",400);
            }
            checkUniqueFavorite(getUser().getId(),post,favoritesDao);
            favorites.setPost(getPost(post,postDao));
            return favorites;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
