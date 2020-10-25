package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Cities;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class CityService {
    static Dao<Cities, Integer> cityDao;
    static {
        try {
            cityDao = DaoManager.createDao(DatabaseConfiguration.connectionSource, Cities.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void checkDoesCityExist(int id) throws SQLException {
        if(cityDao.queryForId(id) == null){
            throw new WebException("Такого города пока нет в нашей базе",404);
        }
    }

    public static Cities getCity(int id) throws SQLException {
        return cityDao.queryForId(id);
    }



}
