package org.winterblade.minecraft.harmony.config;

import com.google.gson.*;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Matt on 4/5/2016.
 */
public class ConfigOperationDeserializer implements JsonDeserializer<IRecipeOperation> {
    private final static Map<String, Class> deserializerMap = new TreeMap<>();

    @Override
    public IRecipeOperation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String type = jsonObject.get("type").getAsString();

        // Make sure we have a type:
        if(type == null) throw new RuntimeException("Unable to read type of operation.");

        // And that it exists:
        type = type.toLowerCase();
        if(!deserializerMap.containsKey(type)) throw new RuntimeException("Unknown type " + type + " for operation.");

        // Now convert it:
        return context.deserialize(json, deserializerMap.get(type));
    }

    public static void CreateDeserializers(Map<String, Class> deserializers) {
        for(Map.Entry<String, Class> deserializer : deserializers.entrySet()) {
            deserializerMap.put(deserializer.getKey(), deserializer.getValue());
        }
    }
}
