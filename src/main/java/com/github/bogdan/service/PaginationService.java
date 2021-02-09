package com.github.bogdan.service;

import com.github.bogdan.controller.MainController;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class PaginationService {
    public static int defaultPagesSize = 3;
    public static int defaultPage = 1;

    public static <T> ArrayList<T> getPagination(Dao<T, Integer> dao, int page, int size) throws SQLException {
        ArrayList<T> list;
        long startRow = (page - 1) * size;
        long maxRows = startRow + size;
        QueryBuilder<T, Integer> queryBuilder = dao.queryBuilder();
        queryBuilder.offset(startRow).limit(maxRows);
        list = (ArrayList<T>) dao.query(queryBuilder.prepare());
        return list;
    }

    static Logger LOGGER = LoggerFactory.getLogger(PaginationService.class);

    public static <T> ArrayList<T> getPagination(ArrayList<T> dao, int page, int size) {
        ArrayList<T> list = new ArrayList<>();
        int startRow = (page - 1) * size;
        long maxRows = startRow + size;
        for (; startRow < maxRows; startRow++) {
            LOGGER.info("startRow: " + startRow + " list.size() " + list.size());
            if (dao.size() > startRow) {
                list.add(dao.get(startRow));
            }
        }
        return list;
    }

    public static <T> int getPages( ArrayList<T> list, int size) throws SQLException {
        int a = 0;
        if (!list.isEmpty()) {
            LOGGER.info("total-page: "+a);
            if (list.size() % size != 0) {
                a = list.size() / size + 1;
            } else if(list.size()< size){
                a = 1;
            }else a = list.size() / size;
        } else a = 1;
        return (int) Math.ceil(a);
    }
    public static <T> int getDaoPages( Dao<T,Integer> dao, int size) throws SQLException {
        int a = 0;
        for(T obj:dao.queryForAll()){
            a++;
        }
        if (a % size != 0) {
            a = a / size + 1;
        } else if(a < size){
            a = 1;
        }else a = a / size;
        return (int) Math.ceil(a);
    }
}
