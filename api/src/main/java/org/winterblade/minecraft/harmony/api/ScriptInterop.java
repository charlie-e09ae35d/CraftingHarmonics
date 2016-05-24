package org.winterblade.minecraft.harmony.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to denote an interop class that will be provided to the scripting engine; any public fields/methods will be
 * accessible inside a JS function by referencing interop.package.path.of.wrapped.Class.
 *
 * If you are using this as a wrapper around an obfuscated class, specify wraps()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScriptInterop {
    Class wraps() default Object.class;
}
