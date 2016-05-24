package org.winterblade.minecraft.harmony.scripting.wrappers.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.ScriptInterop;

/**
 * Created by Matt on 5/22/2016.
 */
@ScriptInterop(wraps = World.class)
public class InteropWorld {
    private final net.minecraft.world.World wrappedWorld;

    public InteropWorld(net.minecraft.world.World wrappedWorld) {
        this.wrappedWorld = wrappedWorld;
    }

    public void spawnEntityInWorld(Entity entity) {
        wrappedWorld.spawnEntityInWorld(entity);
    }
}
