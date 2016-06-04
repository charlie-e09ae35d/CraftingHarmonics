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
@EntityCallback(name = "questingResetQuest")
public class ResetQuestCallback extends BaseEntityCallback {
    /*
     * Serialized properties
     */
    private String quest;
    private IEntityCallbackContainer[] onSuccess;
    private IEntityCallbackContainer[] onFailure;
    private IEntityCallbackContainer[] onComplete;

    @Override
    protected void applyTo(Entity target) {
        if(!EntityPlayerMP.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not resetting '{}' for  '{}' as it's not a player.", quest, target.getName());
            runCallbacks(onFailure, target);
            runCallbacks(onComplete, target);
            return;
        }

        try {
            runCallbacks(QuestRegistry.instance.resetQuest(quest, (EntityPlayerMP) target) ? onSuccess : onFailure, target);
            runCallbacks(onComplete, target);
        } catch (Exception ex) {
            LogHelper.warn("Error resetting quest '{}' for '{}'.", quest, target.getName(), ex);
            runCallbacks(onFailure, target);
            runCallbacks(onComplete, target);
        }
    }
}