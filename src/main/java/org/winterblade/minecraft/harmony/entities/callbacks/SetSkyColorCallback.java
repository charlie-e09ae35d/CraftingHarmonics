package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.messaging.PacketHandler;
import org.winterblade.minecraft.harmony.messaging.server.SkyColorSync;
import org.winterblade.minecraft.harmony.world.sky.SkyColorMapData;

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
    private IEntityCallbackContainer[] onSuccess;
    private IEntityCallbackContainer[] onFailure;
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
        // If we're doing it for everyone...
        if(global) {
            PacketHandler.wrapper.sendToAll(new SkyColorSync(targetDim, transitionTime, colormap));
            runCallbacks(onSuccess, target);
            runCallbacks(onComplete, target);
        }

        // Or just this player...
        if(!EntityPlayerMP.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not setting the sky color for target ({}), as they aren't a player.", target.getName());
            runCallbacks(onFailure, target);
            runCallbacks(onComplete, target);
            return;
        }

        PacketHandler.wrapper.sendTo(new SkyColorSync(targetDim, transitionTime, colormap), (EntityPlayerMP) target);

        runCallbacks(onSuccess, target);
        runCallbacks(onComplete, target);
    }

}
