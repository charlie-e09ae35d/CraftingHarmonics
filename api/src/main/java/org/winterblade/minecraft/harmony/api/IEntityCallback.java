package org.winterblade.minecraft.harmony.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * Created by Matt on 5/22/2016.
 */
@FunctionalInterface
public interface IEntityCallback {
    void apply(EntityLivingBase target, World world);
}
