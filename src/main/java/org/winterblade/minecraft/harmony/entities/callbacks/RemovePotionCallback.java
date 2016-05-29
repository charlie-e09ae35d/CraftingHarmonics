package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "removePotion")
public class RemovePotionCallback extends BaseEntityCallback {
    /*
     * Serialized properties
     */
    private Potion what;
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
        if(what == null) throw new RuntimeException("removePotion callback has no valid potion.");

        if(onSuccess == null) onSuccess = new IEntityCallbackContainer[0];
        if(onFailure == null) onFailure = new IEntityCallbackContainer[0];
        if(onComplete == null) onComplete = new IEntityCallbackContainer[0];
    }

    @Override
    protected void applyTo(Entity target) {
        if(!EntityLivingBase.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not removing potion '{}' from target ({}) as it isn't a mob.", what.getName(), target.getClass().getName());
            return;
        }

        // Run our callbacks
        EntityLivingBase entity = (EntityLivingBase)target;
        if(entity.isPotionActive(what)) {
            runCallbacks(onSuccess, target);
            entity.removePotionEffect(what);
        } else {
            runCallbacks(onFailure, target);
        }

        runCallbacks(onComplete, target);
    }
}
