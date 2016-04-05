package org.winterblade.minecraft.harmony.config;

import com.google.gson.*;

/**
 * Created by Matt on 4/5/2016.
 */
public class ConfigFile {
    public String name;
    public String description;

    /**
     * Generates a new config file
     * @param name          The name of the config set
     * @param description   A description of the set; optional.
     */
    public ConfigFile(String name, String description) {
        this.name = name;
        this.description = description != null ? description : "";
    }

    /**
     * Deserialize JSON into a ConfigFile
     * @param json  The JSON to deserialize
     * @return      The ConfigFile
     */
    public static ConfigFile Deserialize(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, ConfigFile.class);
    }
}
