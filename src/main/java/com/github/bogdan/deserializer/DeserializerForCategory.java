package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.model.Category;

import java.io.IOException;
import java.sql.SQLException;

import static com.github.bogdan.service.CategoryService.checkDoesSuchCategoryExist;
import static com.github.bogdan.service.DeserializerService.getStringFieldValue;

public class DeserializerForCategory extends StdDeserializer<Category> {
    public DeserializerForCategory() {
        super(Category.class);
    }

    public DeserializerForCategory(int id) {
        super(Category.class);
        this.id = id;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Category deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Category a = new Category();

        a.setName(getStringFieldValue(node,"name"));
        try {
            checkDoesSuchCategoryExist(a.getName());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        a.setId(id);
        return a;
    }
}
