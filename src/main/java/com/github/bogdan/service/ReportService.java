package com.github.bogdan.service;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.Cities;
import com.github.bogdan.model.Report;
import com.github.bogdan.model.ReportMessageCategory;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class ReportService {
    public static Dao<ReportMessageCategory, Integer> reportMessageCategoryDao;
    public static Dao<Report,Integer> reportDao;
    static {
        try {
            reportMessageCategoryDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,ReportMessageCategory.class);
            reportDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Report.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void checkDoesSuchReportMessageCategoryExist(int id) throws SQLException {
        if(reportMessageCategoryDao.queryForId(id)==null){
            throw new WebException("Такой категории не существует",400);
        }
    }
}
