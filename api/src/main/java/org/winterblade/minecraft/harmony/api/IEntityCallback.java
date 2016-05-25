package org.winterblade.minecraft.harmony.api;

import net.minecraft.entity.Entity;

import java.util.Set;

/**
 * Created by Matt on 5/22/2016.
 */
public interface IEntityCallback {
    void apply(Entity target);
}

