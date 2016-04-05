package org.winterblade.minecraft.harmony.config;

import com.google.gson.*;

/**
 * Created by Matt on 4/5/2016.
 */
public class ConfigFile {
    public final String name;
    public final String description;

    public ConfigFile(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static ConfigFile Deserialize(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, ConfigFile.class);
    }
}
