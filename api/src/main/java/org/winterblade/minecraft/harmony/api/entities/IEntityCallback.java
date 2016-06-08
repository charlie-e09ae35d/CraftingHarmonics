package org.winterblade.minecraft.harmony.api.entities;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

/**
 * The actual callback for a method; needs to be annotated with @EntityCallback.  See {@link EntityCallback} for more
 * information on what you need to do.
 */
public interface IEntityCallback {
    void apply(Entity target, CallbackMetadata metadata);
}