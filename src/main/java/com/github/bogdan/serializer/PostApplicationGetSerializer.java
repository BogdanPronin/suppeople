package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.model.PostApplication;

import java.io.IOException;

public class PostApplicationGetSerializer extends StdSerializer<PostApplication> {
    public PostApplicationGetSerializer() {
        super(PostApplication.class);
    }

    @Override
    public void serialize(PostApplication postApplication, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("applicationUser",postApplication.getUser());
        jsonGenerator.writeStringField("message",postApplication.getMessage());
        jsonGenerator.writeEndObject();
    }
}
