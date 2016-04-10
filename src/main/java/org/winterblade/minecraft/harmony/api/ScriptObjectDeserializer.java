package org.winterblade.minecraft.harmony.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to define a custom ScriptObject deserializer.
 * You must implement IScriptObjectDeserializer and have a parameterless constructor.
 * Created by Matt on 4/9/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScriptObjectDeserializer {
    Class deserializes();
}
