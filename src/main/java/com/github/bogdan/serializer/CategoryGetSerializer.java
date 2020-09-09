package com.github.bogdan.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.bogdan.model.Category;

import java.io.IOException;

public class CategoryGetSerializer extends StdSerializer<Category> {
    public CategoryGetSerializer() {
        super(Category.class);
    }

    @Override
    public void serialize(Category areaOfActivity, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",areaOfActivity.getId());
        jsonGenerator.writeStringField("name",areaOfActivity.getName());
        jsonGenerator.writeEndObject();
    }
}
