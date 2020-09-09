package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.PostApplication;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.PostApplicationService.getPostApplications;

public class PostGetSerializer extends StdSerializer<Post> {

    public PostGetSerializer() {
        super(Post.class);
    }

    @Override
    public void serialize(Post post, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",post.getId());
        jsonGenerator.writeObjectField("user",post.getUser());
        jsonGenerator.writeObjectField("areaOfActivity",post.getCategory());
        jsonGenerator.writeStringField("task",post.getMessage());
        jsonGenerator.writeStringField("city",post.getCity());
        jsonGenerator.writeStringField("country",post.getImage());
        jsonGenerator.writeStringField("dateOfCreate",post.getDateOfCreate());
        jsonGenerator.writeArrayFieldStart("postApplications");
        try {
            for(PostApplication p: getPostApplications(post.getId())){
                jsonGenerator.writeObject(p);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
