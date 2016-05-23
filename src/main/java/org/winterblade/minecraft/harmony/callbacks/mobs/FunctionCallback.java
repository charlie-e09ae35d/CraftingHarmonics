package org.winterblade.minecraft.harmony.callbacks.mobs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.IEntityCallback;
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
    public void apply(EntityLivingBase target, World world) {
        callback.apply(new InteropEntityLivingBase(target), new InteropWorld(world));
    }

    @FunctionalInterface
    public static interface JSCallback {
        void apply(InteropEntityLivingBase entity, InteropWorld world);
    }
}
