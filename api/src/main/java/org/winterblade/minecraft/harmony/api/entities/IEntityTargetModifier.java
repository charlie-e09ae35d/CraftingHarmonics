package org.winterblade.minecraft.harmony.api.entities;

import net.minecraft.entity.Entity;

import java.util.Set;

/**
 * Created by Matt on 5/24/2016.
 */
public interface IEntityTargetModifier {
    Set<Entity> getTargets(Entity source);
}
