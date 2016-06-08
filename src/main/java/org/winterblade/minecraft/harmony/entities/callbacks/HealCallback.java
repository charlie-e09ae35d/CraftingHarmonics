package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "heal")
public class HealCallback extends BaseEntityCallback {
    /*
     * Serialized properties
     */
    private float amount;
    private IEntityCallback[] onComplete;

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        if(amount < 0) {
            LogHelper.warn("Cannot heal less than 0 damage.");
            amount = 0;
        }
    }

    @Override
    protected void applyTo(Entity target, CallbackMetadata data) {
        if(!EntityLivingBase.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not healing target ({}) as it isn't a mob.", target.getClass().getName());
            return;
        }

        ((EntityLivingBase)target).heal(amount);
        runCallbacks(onComplete, target);
    }
}
