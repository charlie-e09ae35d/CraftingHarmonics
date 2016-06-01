package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.world.sky.ClientSkyModifications;
import org.winterblade.minecraft.harmony.world.sky.SkyColorMapData;

/**
 * Created by Matt on 5/31/2016.
 */
@EntityCallback(name = "setSkyColor")
public class SetSkyColorCallback extends BaseEntityAndDimensionCallback {
    /*
     * Serialized props
     */
    private int transitionTime;
    private SkyColorMapData[] colormap;
    private IEntityCallbackContainer[] onComplete;

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        if(colormap == null) throw new RuntimeException("setSkyColor's 'colormap' property must be provided.");
    }

    @Override
    protected void applyWithTargetDimension(Entity target, int targetDim) {
        // TODO: Make this work on the server too. :)
        ClientSkyModifications.transition(targetDim, transitionTime, colormap);
        runCallbacks(onComplete, target);
    }

}
