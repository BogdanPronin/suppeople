package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.PostApplication;

import java.io.IOException;
import java.util.ArrayList;

public class PostApplicationSerializer extends StdSerializer<PostApplication> {
    public PostApplicationSerializer() {
        super(PostApplication.class);
    }
    @Override
    public void serialize(PostApplication postApplication, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",postApplication.getId());
        jsonGenerator.writeNumberField("postId",postApplication.getPost().getId());
        jsonGenerator.writeObjectField("applicationUser",postApplication.getUser());
        jsonGenerator.writeStringField("message",postApplication.getMessage());
        if(postApplication.getStatus()!=null){
            jsonGenerator.writeStringField("status",postApplication.getStatus().toString());
        }else jsonGenerator.writeStringField("status",null);
        jsonGenerator.writeEndObject();
    }
}
