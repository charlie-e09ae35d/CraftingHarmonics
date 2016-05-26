package org.winterblade.minecraft.harmony.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation lets Crafting Harmonics know that it should register a deserializer for this type of object
 * The name provided here must match the expected "type" of the object in the config file
 * (though names will be case insensitive when being matched to the type)
 *
 * Required parameters:
 * You should have a parameterized constructor for these classes, which accepts the data you want
 * to receive from the JSON.  This will be passed through the ScriptObjectReader first, so, if you have a custom type,
 * make sure you register a deserializer for it.
 *
 * Optional parameters:
 * For optional parameters, you may either provide them in a parameterized constructor (optionally with the
 * {@link ComponentParameter} annotation), with a setProperty method, or, lastly, by being a non-final field
 * on the object.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TypedObject {
    String name();

    String[] properties();
}