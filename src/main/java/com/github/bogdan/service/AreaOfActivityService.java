package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.AreaOfActivity;
import com.github.bogdan.model.Post;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class AreaOfActivityService {
    public static Dao<AreaOfActivity,Integer> areaOfActivityDao;

    static {
        try {
            areaOfActivityDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,AreaOfActivity.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void checkDoesSuchAreaOfActivityExist(int id) throws SQLException {
        if(areaOfActivityDao.queryForId(id)==null){
            throw new WebException("Such area of activity isn't exist",400);
        }
    }

    public static AreaOfActivity getAreaOfActivity(int id) throws SQLException {
        return areaOfActivityDao.queryForId(id);
    }
}
