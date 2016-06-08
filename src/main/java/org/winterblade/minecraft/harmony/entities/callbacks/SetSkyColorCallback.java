package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.world.sky.SkyColorMapData;
import org.winterblade.minecraft.harmony.world.sky.SkyModificationRegistry;

/**
 * Created by Matt on 5/31/2016.
 */
@EntityCallback(name = "setSkyColor")
public class SetSkyColorCallback extends BaseEntityAndDimensionCallback {
    /*
     * Serialized props
     */
    private boolean global;
    private int transitionTime;
    private SkyColorMapData[] colormap;
    private IEntityCallback[] onSuccess;
    private IEntityCallback[] onFailure;
    private IEntityCallback[] onComplete;

    /*
     * Computed properties
     */
    private transient String hash;

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        if(colormap == null) throw new RuntimeException("setSkyColor's 'colormap' property must be provided.");

        hash = SkyColorMapData.getHash(colormap);
    }

    @Override
    protected void applyWithTargetDimension(Entity target, int targetDim) {
        // If we're doing it for everyone...
        if(global) {
            SkyModificationRegistry.runModification(new SkyModificationRegistry.Data(targetDim, transitionTime, colormap, hash));
            runCallbacks(onSuccess, target);
            runCallbacks(onComplete, target);
            return;
        }

        // Otherwise, try to run it on the target...
        runCallbacks(SkyModificationRegistry.runModificationOn(target,
                new SkyModificationRegistry.Data(targetDim, transitionTime, colormap, hash))
                    ? onSuccess
                    : onFailure,
                target);
        runCallbacks(onComplete, target);
    }

}
