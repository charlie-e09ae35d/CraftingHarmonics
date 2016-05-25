package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.IEntityCallback;
import org.winterblade.minecraft.harmony.scripting.wrappers.entity.InteropEntity;
import org.winterblade.minecraft.harmony.scripting.wrappers.world.InteropWorld;

/**
 * Created by Matt on 5/22/2016.
 */
@Component(properties = {"function"})
public class FunctionCallback implements IEntityCallback {
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
