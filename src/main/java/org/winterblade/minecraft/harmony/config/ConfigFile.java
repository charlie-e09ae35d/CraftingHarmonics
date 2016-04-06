package org.winterblade.minecraft.harmony.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;

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
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
        gsonBuilder.registerTypeAdapter(IRecipeOperation.class, new ConfigOperationDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, ConfigFile.class);
    }
}
