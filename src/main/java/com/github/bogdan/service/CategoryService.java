package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Category;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class CategoryService {
    public static Dao<Category,Integer> categoryDao;

    static {
        try {
            categoryDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Category.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void checkDoesSuchCategoryExist(int id) throws SQLException {
        if(categoryDao.queryForId(id)==null){
            throw new WebException("Such area of activity isn't exist",400);
        }
    }

    public static void checkDoesSuchCategoryExist(String name) throws SQLException {
        for(Category a: categoryDao.queryForAll()){
            if(a.getName().equals(name)){
                throw new WebException("Such area of activity is already exist",400);
            }
        }
    }

    public static Category getCategory(int id) throws SQLException {
        return categoryDao.queryForId(id);
    }
}
