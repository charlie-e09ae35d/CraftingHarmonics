package org.winterblade.minecraft.harmony.entities.callbacks.quest;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.entities.callbacks.BaseEntityCallback;
import org.winterblade.minecraft.harmony.quests.QuestRegistry;

/**
 * Created by Matt on 5/30/2016.
 */
@EntityCallback(name = "questingTakeLives")
public class TakeLivesCallback extends BaseEntityCallback {
    /*
     * Serialized properties
     */
    private int lives;
    private IEntityCallback[] onSuccess;
    private IEntityCallback[] onFailure;
    private IEntityCallback[] onComplete;

    @Override
    protected void applyTo(Entity target, CallbackMetadata data) {
        if(!EntityPlayerMP.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not taking lives from '{}' as it's not a player.",target.getName());
            runCallbacks(onFailure, target, data);
            runCallbacks(onComplete, target, data);
            return;
        }

        int livesTaken = QuestRegistry.instance.takeLives((EntityPlayerMP) target, lives);
        runCallbacks(lives <= livesTaken ? onSuccess : onFailure, target, data);
        runCallbacks(onComplete, target, data);
    }
}