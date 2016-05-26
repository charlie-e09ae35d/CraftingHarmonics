package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.api.TypedObject;
import org.winterblade.minecraft.harmony.scripting.wrappers.entity.InteropEntity;
import org.winterblade.minecraft.harmony.scripting.wrappers.world.InteropWorld;

/**
 * Created by Matt on 5/22/2016.
 */
@TypedObject(name = "function", properties = {"function"})
public class FunctionCallback implements IEntityCallbackContainer {
    private final JSCallback callback;

    public FunctionCallback(JSCallback callback) {
        this.callback = callback;
    }

    @Override
    public void apply(Entity target) {
        callback.apply(new InteropEntity(target), new InteropWorld(target.getEntityWorld()));
    }

    @FunctionalInterface
    public static interface JSCallback {
        void apply(InteropEntity entity, InteropWorld world);
    }
}
