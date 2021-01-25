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


    public static void checkDoesSuchReportMessageCategoryExist(int id,Dao<ReportMessageCategory, Integer> reportMessageCategoryDao) throws SQLException {
        if(reportMessageCategoryDao.queryForId(id)==null){
            throw new WebException("Такой категории не существует",400);
        }
    }
}
