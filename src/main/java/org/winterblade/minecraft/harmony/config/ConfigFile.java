package org.winterblade.minecraft.harmony.config;

import com.google.gson.*;
import org.winterblade.minecraft.harmony.config.operations.ConfigOperation;
import org.winterblade.minecraft.harmony.config.operations.ConfigOperationDeserializer;

import java.util.List;

/**
 * Created by Matt on 4/5/2016.
 */
public class ConfigFile {
    public String name;
    public String description;
    public List<ConfigSet> sets;

    /**
     * Deserialize JSON into a ConfigFile
     * @param json  The JSON to deserialize
     * @return      The ConfigFile
     */
    public static ConfigFile Deserialize(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.registerTypeAdapter(ConfigOperation.class, new ConfigOperationDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, ConfigFile.class);
    }
}
