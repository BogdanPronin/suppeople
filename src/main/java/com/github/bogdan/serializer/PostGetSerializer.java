package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.PostApplication;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.PostApplicationService.getPostApplications;

public class PostGetSerializer extends StdSerializer<Post> {

    public PostGetSerializer(Dao<PostApplication, Integer> postApplicationDao) {
        super(Post.class);
        this.postApplicationDao = postApplicationDao;
    }
    private Dao<PostApplication, Integer> postApplicationDao;

    @Override
    public void serialize(Post post, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",post.getId());
        jsonGenerator.writeObjectField("user",post.getUser());
        jsonGenerator.writeObjectField("category",post.getCategory());
        jsonGenerator.writeStringField("message",post.getMessage());
        jsonGenerator.writeObjectField("city",post.getCity());
        jsonGenerator.writeStringField("image",post.getImage());
        jsonGenerator.writeStringField("dateOfCreate",post.getDateOfCreate());
        if(post.getStatus()!=null) {
            jsonGenerator.writeStringField("status", post.getStatus().toString());
        }else jsonGenerator.writeStringField("status",null);

        jsonGenerator.writeArrayFieldStart("postApplications");
        try {
            for(PostApplication p: getPostApplications(post.getId(),postApplicationDao)){
                jsonGenerator.writeObject(p);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
