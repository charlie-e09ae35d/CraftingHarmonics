package org.winterblade.minecraft.harmony.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation lets Crafting Harmonics know that it should register a deserializer for this type of operation
 * The name provided here must match the expected "type" of the operation in the config file
 * (though names will be case insensitive when being matched to the type)
 *
 * You must also create a class that extends BasicOperation (-NOT- IOperation)
 *
 * Created by Matt on 4/6/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Operation {
    String name();

    String dependsOn() default "";
}
