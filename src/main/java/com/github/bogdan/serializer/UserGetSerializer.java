package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.model.User;

import java.io.IOException;

public class UserGetSerializer extends StdSerializer<User> {
    public UserGetSerializer() {
        super(User.class);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",user.getId());
        jsonGenerator.writeStringField("fname",user.getFname());
        jsonGenerator.writeStringField("lname",user.getLname());
        jsonGenerator.writeStringField("login",user.getLogin());
        jsonGenerator.writeStringField("dateOfBirthday",user.getDateOfBirthday());
        jsonGenerator.writeStringField("dateOfRegister",user.getDateOfRegister());
        jsonGenerator.writeStringField("phone",user.getPhone());
        jsonGenerator.writeStringField("email",user.getEmail());
        jsonGenerator.writeStringField("city",user.getCity());
        jsonGenerator.writeArrayFieldStart("areaOfActivities");

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
