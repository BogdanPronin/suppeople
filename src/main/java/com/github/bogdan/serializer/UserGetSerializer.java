package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.model.User;
import com.github.bogdan.model.UserArea;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;

import static com.github.bogdan.service.ClassService.getFieldsName;
import static com.github.bogdan.service.UserService.getUsersAreas;

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
        jsonGenerator.writeStringField("country",user.getCountry());
        jsonGenerator.writeStringField("city",user.getCity());
        jsonGenerator.writeArrayFieldStart("areaOfActivities");
        try {
            for(UserArea u : getUsersAreas(user)){
                jsonGenerator.writeObject(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
