package com.github.bogdan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.deserializer.DeserializerForAddUser;
import com.github.bogdan.model.User;
import com.github.bogdan.serializer.UserGetSerializer;
import com.github.bogdan.service.SortingService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static com.github.bogdan.service.CtxService.*;
import static com.github.bogdan.service.PaginationService.getPagination;
import static com.github.bogdan.service.SortingService.sortByQueryParams;

public class UserController {
    static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

}
