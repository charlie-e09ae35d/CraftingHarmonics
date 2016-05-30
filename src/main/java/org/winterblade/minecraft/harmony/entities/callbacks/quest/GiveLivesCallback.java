package org.winterblade.minecraft.harmony.entities.callbacks.quest;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.entities.callbacks.BaseEntityCallback;
import org.winterblade.minecraft.harmony.quests.QuestRegistry;

/**
 * Created by Matt on 5/30/2016.
 */
@EntityCallback(name = "questingGiveLives")
public class GiveLivesCallback extends BaseEntityCallback {
    /*
     * Serialized properties
     */
    private int lives;
    private IEntityCallbackContainer[] onSuccess;
    private IEntityCallbackContainer[] onFailure;
    private IEntityCallbackContainer[] onComplete;

    @Override
    protected void applyTo(Entity target) {
        if(!EntityPlayerMP.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not giving lives to '{}' as it's not a player.",target.getName());
            runCallbacks(onFailure, target);
            runCallbacks(onComplete, target);
            return;
        }

        int livesGiven = QuestRegistry.instance.giveLives((EntityPlayerMP) target, lives);
        runCallbacks(lives <= livesGiven ? onSuccess : onFailure, target);
        runCallbacks(onComplete, target);
    }
}
