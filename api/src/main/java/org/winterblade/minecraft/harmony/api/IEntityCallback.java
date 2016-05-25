package org.winterblade.minecraft.harmony.api;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * Created by Matt on 5/22/2016.
 */
@FunctionalInterface
public interface IEntityCallback {
    void apply(Entity target, World world);
}
