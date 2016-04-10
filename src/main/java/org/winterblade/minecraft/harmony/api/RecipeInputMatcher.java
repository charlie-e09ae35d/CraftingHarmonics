package org.winterblade.minecraft.harmony.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies a matcher to be applied to inputs while trying to determine if a recipe matches.  Unlike most of the rest
 * of the interfaces, you should have a parameterized constructor for these classes, which accepts the data you want
 * to receive from the JSON.  This will be passed through the ScriptObjectReader first, so, if you have a custom type,
 * make sure you register a deserializer for it.
 *
 * Keep in mind these are run twice per recipe, so, keep it simple or set your priority quite low.
 *
 * Created by Matt on 4/9/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RecipeInputMatcher {
    String[] properties();

    String[] aliases() default {};

    Priority priority() default Priority.LOW;
}

