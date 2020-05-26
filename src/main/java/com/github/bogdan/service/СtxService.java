package com.github.bogdan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bogdan.model.JsonMessage;
import io.javalin.http.Context;

public class СtxService {
    public static void created(Context ctx) throws JsonProcessingException {
        stringCtxObjectValue("Created",200,ctx);
    }
    public static void deleted(Context ctx) throws JsonProcessingException {
        stringCtxObjectValue("Deleted",200,ctx);
    }
    public static void updated(Context ctx) throws JsonProcessingException {
        stringCtxObjectValue("Updated",200,ctx);
    }
    public static void stringCtxObjectValue(String message, int status,Context ctx) throws JsonProcessingException {
        JsonMessage jsonMessage = new JsonMessage(message,status);
        ObjectMapper objectMapper = new ObjectMapper();
        ctx.result(objectMapper.writeValueAsString(jsonMessage));
        ctx.status(status);
    }
}
