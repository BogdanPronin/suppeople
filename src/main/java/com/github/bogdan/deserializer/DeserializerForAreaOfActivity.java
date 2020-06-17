package com.github.bogdan.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.bogdan.model.AreaOfActivity;
import com.github.bogdan.model.User;

import java.io.IOException;

import static com.github.bogdan.service.DeserializerService.checkNullStringFieldValue;

public class DeserializerForAreaOfActivity extends StdDeserializer<AreaOfActivity> {
    public DeserializerForAreaOfActivity() {
        super(AreaOfActivity.class);
    }

    @Override
    public AreaOfActivity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        AreaOfActivity a = new AreaOfActivity();

        a.setName(checkNullStringFieldValue(node,"name"));

        return a;
    }
}
