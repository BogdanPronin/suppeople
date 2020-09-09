package com.github.bogdan.service;

import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SortingService {
    static Logger LOGGER = LoggerFactory.getLogger(SortingService.class);

    public static <T>ArrayList<T> sortByQueryParams(Dao<T,Integer> dao, Class<T> tClass, ArrayList<String> queryParams, Context ctx) throws NoSuchFieldException, SQLException, IllegalAccessException {
        ArrayList<T> objects = new ArrayList<>();
        for(String s:queryParams){
            Field field = tClass.getDeclaredField(s);
            if (ctx.queryParam(s)!=null) {
                field.setAccessible(true);
                for (T obj : dao.queryForAll()) {
                    Object value = field.get(obj);
                    if(ctx.queryParam(s).equals(value.toString())){
                        objects.add(obj);
                    }
                }
                field.setAccessible(false);
            }
        }
        return objects;
    }
    private static List<String> getProperties(Method[] methods) {
        List<String> methodNames = new ArrayList<>();
        for (Method method : methods) {
            String name = method.getName().substring(3);
            methodNames.add(name);
        }
        return methodNames;
    }
}
