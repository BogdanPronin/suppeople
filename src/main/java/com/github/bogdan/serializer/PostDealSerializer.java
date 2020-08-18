package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.model.Deal;
import com.github.bogdan.model.Post;

import java.io.IOException;

public class PostDealSerializer extends StdSerializer<Post> {
    public PostDealSerializer() {
        super(Post.class);
    }

    @Override
    public void serialize(Post post, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",post.getId());
        jsonGenerator.writeObjectField("user",post.getUser());
        jsonGenerator.writeObjectField("areaOfActivity",post.getAreaOfActivity());
        jsonGenerator.writeStringField("task",post.getTask());
        jsonGenerator.writeObjectField("deadline",post.getDeadline());
        jsonGenerator.writeStringField("city",post.getCity());
        jsonGenerator.writeStringField("country",post.getCountry());
        jsonGenerator.writeStringField("dateOfCreate",post.getDateOfCreate());
        jsonGenerator.writeEndObject();
    }
}
