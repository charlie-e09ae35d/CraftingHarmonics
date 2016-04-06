package org.winterblade.minecraft.harmony.api;

/**
 * This annotation lets Crafting Harmonics know that it should register a deserializer for this type of operation
 * The name provided here must match the expected "type" of the operation in the config file
 * (though names will be case insensitive when being matched to the type)
 * Created by Matt on 4/6/2016.
 */
public @interface RecipeOperation {
    String name();
}
