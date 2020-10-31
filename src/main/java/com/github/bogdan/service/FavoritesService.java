package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Favorites;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class FavoritesService {
    static Dao<Favorites, Integer> favoritesDao;
    static {
        try {
            favoritesDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Favorites.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void checkUniqueFavorite(int userId,int postId) throws SQLException {
        for(Favorites f: favoritesDao.queryForAll()){
            if(f.getUser().getId() == userId && f.getPost().getId() == postId){
                throw new WebException("У вас уже есть этот пост в избранных",400);
            }
        }
    }
}
