package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.model.UserArea;

import java.io.IOException;

public class UserAreaSerializerForUser extends StdSerializer<UserArea> {
    public UserAreaSerializerForUser() {
        super(UserArea.class);
    }

    @Override
    public void serialize(UserArea userArea, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",userArea.getId());
        jsonGenerator.writeObjectField("areaOfActivity",userArea.getAreaOfActivity());
        jsonGenerator.writeStringField("experience",userArea.getExperience());
        jsonGenerator.writeNumberField("taskExecutionSpeed",userArea.getTaskExecutionSpeed());
        jsonGenerator.writeEndObject();
    }
}
