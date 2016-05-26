package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.scripting.wrappers.entity.InteropEntity;
import org.winterblade.minecraft.harmony.scripting.wrappers.world.InteropWorld;

/**
 * Created by Matt on 5/22/2016.
 */
@EntityCallback(name = "function")
public class FunctionCallback extends BaseEntityCallback {
    private JSCallback callback;

    public FunctionCallback() {}

    public FunctionCallback(JSCallback callback) {
        this.callback = callback;
    }

    @Override
    public void applyTo(Entity target) {
        callback.apply(new InteropEntity(target), new InteropWorld(target.getEntityWorld()));
    }

    @FunctionalInterface
    public static interface JSCallback {
        void apply(InteropEntity entity, InteropWorld world);
    }
}
