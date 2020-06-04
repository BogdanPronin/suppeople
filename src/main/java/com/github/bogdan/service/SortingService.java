package com.github.bogdan.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SortingService {
    static Logger LOGGER = LoggerFactory.getLogger(SortingService.class);

    public static<T>List<String> sortByQueryParam(Class<T> tClass){
        Method[] methods = tClass.getDeclaredMethods();
        List<String> actualMethods = getProperties(methods);
        LOGGER.info(String.valueOf(methods.length));
        return actualMethods;
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
