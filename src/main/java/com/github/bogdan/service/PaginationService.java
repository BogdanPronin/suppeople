package com.github.bogdan.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;

public class PaginationService {
    public static int defaultPagesSize = 3;
    public static int defaultPage = 1;
    public static <T> ArrayList<T> getPagination(Dao<T,Integer> dao, int page, int size) throws SQLException {
        ArrayList<T> list;
        long startRow = (page-1)*size;
        long maxRows = startRow+size;
        QueryBuilder<T, Integer> queryBuilder = dao.queryBuilder();
        queryBuilder.offset(startRow).limit(maxRows);
        list = (ArrayList<T>) dao.query(queryBuilder.prepare());
        return list;
    }
    public static <T> int getPages(Dao<T,Integer>  dao,ArrayList<T> list, int size) throws SQLException {
        float a;
        if(!list.isEmpty()){
            a = list.size()/size;
        }else a = dao.queryForAll().size()/size;

        return (int) Math.ceil(a);
    }
}
