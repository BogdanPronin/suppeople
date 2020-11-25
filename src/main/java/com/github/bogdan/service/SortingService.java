package com.github.bogdan.service;

import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SortingService {
    static Logger LOGGER = LoggerFactory.getLogger(SortingService.class);

    private static List<String> getProperties(Method[] methods) {
        List<String> methodNames = new ArrayList<>();
        for (Method method : methods) {
            String name = method.getName().substring(3);
            methodNames.add(name);
        }
        return methodNames;
    }

    public static <T>ArrayList<T> getByQueryParams(Dao<T,Integer> dao, Class<T> tClass, ArrayList<String> queryParams, Context ctx) throws NoSuchFieldException, SQLException, IllegalAccessException, UnsupportedEncodingException {
        ArrayList<T> objects = new ArrayList<>();
        for(String s:queryParams){
            Field field = tClass.getDeclaredField(s);
            LOGGER.info("Field " + field.getName());
            String currentParam = ctx.queryParam(s);
            if (currentParam!=null) {
                field.setAccessible(true);
                for (T obj : dao.queryForAll()) {
                    Object value = field.get(obj);
                    String valueString = null;
                    if(value != null){
                        valueString = value.toString();
                    }
                    if(URLDecoder.decode(currentParam, StandardCharsets.UTF_8.toString()).equals(valueString)){
                        objects.add(obj);
                    }
                }
                field.setAccessible(false);
            }
        }
        return objects;
    }
}
