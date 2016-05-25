package org.winterblade.minecraft.harmony.common.callbacks.mobs;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.IEntityCallback;
import org.winterblade.minecraft.harmony.scripting.wrappers.entity.InteropEntity;
import org.winterblade.minecraft.harmony.scripting.wrappers.entity.InteropEntityLivingBase;
import org.winterblade.minecraft.harmony.scripting.wrappers.world.InteropWorld;

/**
 * Created by Matt on 5/22/2016.
 */
public class FunctionCallback implements IEntityCallback {
    private final JSCallback callback;

    public FunctionCallback(JSCallback callback) {
        this.callback = callback;
    }

    @Override
    public void apply(Entity target, World world) {
        callback.apply(new InteropEntity(target), new InteropWorld(world));
    }

    @FunctionalInterface
    public static interface JSCallback {
        void apply(InteropEntity entity, InteropWorld world);
    }
}
