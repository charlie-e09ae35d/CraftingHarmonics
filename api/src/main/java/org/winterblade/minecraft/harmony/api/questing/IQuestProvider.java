package org.winterblade.minecraft.harmony.api.questing;

import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Interface denoting a quest provider; this must be annotated with {@link QuestProvider}.
 */
public interface IQuestProvider {
    /**
     * Gets the name of this provider.
     * @return  The name of the provider, used to reference it in matchers/callbacks.
     */
    String getName();

    /**
     * Gets if hardcore mode is enabled for the given provider.
     * @return  True if it is, false otherwise.  Should return false if the underlying provider doesn't support hardcore.
     */
    boolean isHardcoreModeEnabled();

    /**
     * Gets the {@link QuestStatus} of the given quest, by name, for the given player.
     * @param name      The name of the quest
     * @param player    The player to query
     * @return          The quest status
     */
    QuestStatus getQuestStatus(String name, EntityPlayerMP player);

    /**
     * Give a life (if such a thing is supported) to the given player.
     * @param player    The player to grant a life to.
     * @return          True if a life was granted; false otherwise.
     */
    boolean giveLife(EntityPlayerMP player);

    /**
     * Completes the given quest objectives for the player.
     * @param name      The name of the quest
     * @param player    The player to complete for
     * @return          True if the quest was completed, false otherwise.
     */
    boolean completeQuest(String name, EntityPlayerMP player);

    /**
     * Resets the quest and all its progress for the given player.
     * @param name      The name of the quest
     * @param player    The player to reset
     * @return          True if the quest was reset, false otherwise.
     */
    boolean resetQuest(String name, EntityPlayerMP player);
}
