package org.winterblade.minecraft.harmony.config.operations;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Matt on 4/5/2016.
 */
public class ConfigOperationDeserializer implements JsonDeserializer<IConfigOperation> {
    private static Map<String, Class> map = new TreeMap<String, Class>();

    static {
        map.put("remove", RemoveOperation.class);
        map.put("addshaped", AddShapedOperation.class);
        map.put("addshapeless", AddShapelessOperation.class);
    }

    @Override
    public IConfigOperation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String type = jsonObject.get("type").getAsString();

        // Make sure we have a type:
        if(type == null) throw new RuntimeException("Unable to read type of operation.");

        // And that it exists:
        type = type.toLowerCase();
        if(!map.containsKey(type)) throw new RuntimeException("Unknown type " + type + " for operation.");

        // Now convert it:
        return context.deserialize(json, map.get(type));
    }
}
