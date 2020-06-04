package com.github.bogdan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.User;
import com.github.bogdan.serializer.WebExceptionSerializer;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.github.bogdan.service.ContactService.checkValidateEmail;
import static com.github.bogdan.service.LocalDateService.checkAge;
import static com.github.bogdan.service.SortingService.sortByQueryParam;

public class Test {
    public static void main(String[] args) throws JsonProcessingException, NoSuchMethodException, NoSuchFieldException {
        User u = new User();

    }

}
