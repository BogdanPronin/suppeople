package com.github.bogdan;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.Post;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.UnsupportedEncodingException;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException, SQLException, UnsupportedEncodingException {
        Dao<Post,Integer> postDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Post.class);
//
//        System.out.println(sort(postDao,));
    }
}
