package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Deal;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class DealService {
    public static Dao<Deal,Integer> dealDao;

    static {
        try {
            dealDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Deal.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void checkDoesSuchDealExist(int dealId) throws SQLException {
        if(dealDao.queryForId(dealId)==null){
            throw new WebException("Such deal doesn't exist",400);
        }
    }
    public static void checkIsItUsersDeal(int dealId,int userId) throws SQLException {
        if (dealDao.queryForId(dealId).getPostApplication().getPost().getUser().getId() == userId) {
            return;
        }
        throw new WebException("It isn't your deal",400);
    }
}
