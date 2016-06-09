package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.DimensionManager;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

/**
 * Created by Matt on 5/26/2016.
 */
public abstract class BaseEntityAndDimensionCallback extends BaseEntityCallback {
    /*
             * Serialized properties
             */
    protected int dimension = Integer.MIN_VALUE;

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        if(dimension != Integer.MIN_VALUE && !DimensionManager.isDimensionRegistered(dimension)) {
            throw new RuntimeException("Unable to link to " + dimension + " from '" + this.getClass().getSimpleName() +
                    "', as it isn't registered.");
        }
    }

    @Override
    protected final void applyTo(Entity target, CallbackMetadata data) {
        int targetDim = dimension == Integer.MIN_VALUE ? target.dimension : dimension;
        applyWithTargetDimension(target, targetDim, data);
    }

    protected abstract void applyWithTargetDimension(Entity target, int targetDim, CallbackMetadata metadata);
}
