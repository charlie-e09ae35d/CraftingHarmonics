package org.winterblade.minecraft.harmony.api;

import net.minecraft.entity.Entity;

/**
 * The actual callback for a method; needs to be annotated with @TypedObject.  See {@link TypedObject} for more
 * information on what you need to do.
 */
public interface IEntityCallback {
    void apply(Entity target);
}