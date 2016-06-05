package org.winterblade.minecraft.harmony.api.tileentities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation lets Crafting Harmonics know that it should register a deserializer for this type of object
 * The name provided here must match the expected "type" of the object in the config file
 * (though names will be case insensitive when being matched to the type)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TileEntityCallback {
    String name();

    String dependsOn() default "";
}
