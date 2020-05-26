package com.github.bogdan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.serializer.WebExceptionSerializer;

import static com.github.bogdan.service.ContactService.checkValidateEmail;
import static com.github.bogdan.service.LocalDateService.checkAge;

public class Test {
    public static void main(String[] args) throws JsonProcessingException {

    }
}
