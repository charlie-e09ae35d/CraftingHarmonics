package org.winterblade.minecraft.harmony.api.questing;

import net.minecraft.entity.player.EntityPlayerMP;
import org.winterblade.minecraft.harmony.api.utility.IDependentProvider;

/**
 * Interface denoting a quest provider; this must be annotated with {@link QuestProvider}.
 */
public interface IQuestProvider extends IDependentProvider {
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
     * Checks to see if the given player is in a group
     * @param player    The player to check
     * @return          True if the player is; false otherwise.  Should return false if the underlying provider
     *                  doesn't support groups.
     */
    boolean inParty(EntityPlayerMP player);

    /**
     * Checks to see if the given player is in a group that is sharing lives
     * @param player    The player to check
     * @return          True if the player is; false otherwise.  Should return false if the underlying provider
     *                  doesn't support groups, limited lives, life sharing, or if the player isn't in a group.
     */
    boolean hasSharedLives(EntityPlayerMP player);

    /**
     * Checks to see if the given player is in a group that is sharing loot
     * @param player    The player to check
     * @return          True if the player is; false otherwise.  Should return false if the underlying provider
     *                  doesn't support groups, loot sharing, or if the player isn't in a group.
     */
    boolean hasSharedLoot(EntityPlayerMP player);

    /**
     * Give a number of lives (if such a thing is supported) to the given player.
     * @param player    The player to grant a life to.
     * @param lives     The number of lives to grant.
     * @return          The number of lives granted.
     */
    int giveLives(EntityPlayerMP player, int lives);

    /**
     * Take a number of lives (if such a thing is supported) from the given player.
     * @param player    The player to take a life from.
     * @param lives     The number of lives to take.
     * @return          The number of lives taken.
     */
    int takeLives(EntityPlayerMP player, int lives);

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

    /**
     * Used to reset the internal caches for this provider, if any.
     */
    void resetCache();
}
