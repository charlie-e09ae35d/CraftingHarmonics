package org.winterblade.minecraft.harmony.api;

/**
 * Interface for declaring a particular item should only be registered with the system if the given mod is loaded.
 */

public @interface ModDependent {
    String dependsOn();
}
