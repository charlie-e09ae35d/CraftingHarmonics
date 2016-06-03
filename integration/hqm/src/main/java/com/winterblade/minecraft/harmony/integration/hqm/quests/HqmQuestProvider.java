package com.winterblade.minecraft.harmony.integration.hqm.quests;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import hardcorequesting.quests.Quest;
import hardcorequesting.quests.QuestData;
import hardcorequesting.quests.QuestingData;
import hardcorequesting.team.RewardSetting;
import net.minecraft.entity.player.EntityPlayerMP;
import org.winterblade.minecraft.harmony.api.questing.IQuestProvider;
import org.winterblade.minecraft.harmony.api.questing.QuestProvider;
import org.winterblade.minecraft.harmony.api.questing.QuestStatus;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by Matt on 5/29/2016.
 */
@QuestProvider
public class HqmQuestProvider implements IQuestProvider {
    private static LoadingCache<String, UUID> questCache = CacheBuilder.newBuilder().build(
            // If we haven't loaded it in the pre-load, it doesn't exist.
            new CacheLoader<String, UUID>() {
                @Override
                public UUID load(String key) throws Exception {
                    return null;
                }
            }
    );

    /**
     * Gets the name of this provider.
     *
     * @return The name of the provider, used to reference it in matchers/callbacks.
     */
    @Override
    public String getName() {
        return "hqm";
    }

    /**
     * Get a list of mod dependencies for this provider
     *
     * @return A string array of mod dependencies.
     */
    @Override
    public String[] getDependencyList() {
        return new String[] {"HardcoreQuesting"};
    }

    /**
     * Gets if hardcore mode is enabled for the given provider.
     *
     * @return True if it is, false otherwise.  Should return false if the underlying provider doesn't support hardcore.
     */
    @Override
    public boolean isHardcoreModeEnabled() {
        return QuestingData.isHardcoreActive();
    }

    /**
     * Gets the {@link QuestStatus} of the given quest, by name, for the given player.
     *
     * @param name   The name of the quest
     * @param player The player to query
     * @return The quest status
     */
    @Override
    public QuestStatus getQuestStatus(String name, EntityPlayerMP player) {
        UUID id = getQuestId(name);
        if(id == null) return QuestStatus.INVALID;

        QuestData questData = QuestingData.getQuestingData(player).getTeam().getQuestData(id.toString());
        if(questData == null) return QuestStatus.INVALID;

        if(questData.claimed) return QuestStatus.CLOSED;
        if(questData.completed) return QuestStatus.COMPLETE;
        if(questData.available) return QuestStatus.ACTIVE;

        return QuestStatus.LOCKED;
    }

    /**
     * Checks to see if the given player is in a group
     *
     * @param player The player to check
     * @return True if the player is; false otherwise.  Should return false if the underlying provider
     * doesn't support groups.
     */
    @Override
    public boolean inParty(EntityPlayerMP player) {
        return !QuestingData.getQuestingData(player).getTeam().isSingle();
    }

    /**
     * Checks to see if the given player is in a group that is sharing lives
     *
     * @param player The player to check
     * @return True if the player is; false otherwise.  Should return false if the underlying provider
     * doesn't support groups, limited lives, life sharing, or if the player isn't in a group.
     */
    @Override
    public boolean hasSharedLives(EntityPlayerMP player) {
        return isHardcoreModeEnabled() && inParty(player) && QuestingData.getQuestingData(player).getTeam().isSharingLives();
    }

    /**
     * Checks to see if the given player is in a group that is sharing loot
     *
     * @param player The player to check
     * @return True if the player is; false otherwise.  Should return false if the underlying provider
     * doesn't support groups, loot sharing, or if the player isn't in a group.
     */
    @Override
    public boolean hasSharedLoot(EntityPlayerMP player) {
        return inParty(player) && QuestingData.getQuestingData(player).getTeam().getRewardSetting() == RewardSetting.ALL;
    }

    /**
     * Give a number of lives (if such a thing is supported) to the given player.
     *
     * @param player The player to grant a life to.
     * @param lives  The number of lives to grant.
     * @return The number of lives granted.
     */
    @Override
    public int giveLives(EntityPlayerMP player, int lives) {
        QuestingData data = QuestingData.getQuestingData(player);
        int curLives = data.getLives();
        return data.addLives(player, lives) - curLives;
    }

    /**
     * Take a number of lives (if such a thing is supported) from the given player.
     *
     * @param player The player to take a life from.
     * @param lives  The number of lives to take.
     * @return The number of lives taken.
     */
    @Override
    public int takeLives(EntityPlayerMP player, int lives) {
        QuestingData data = QuestingData.getQuestingData(player);
        int curLives = data.getLives();
        data.removeLives(player, lives);
        return curLives - data.getLives();
    }

    /**
     * Completes the given quest objectives for the player.
     *
     * @param name   The name of the quest
     * @param player The player to complete for
     * @return True if the quest was completed, false otherwise.
     */
    @Override
    public boolean completeQuest(String name, EntityPlayerMP player) {
        UUID id = getQuestId(name);
        if(id == null) return false;

        Quest quest = Quest.getQuest(id.toString());
        if(quest == null) return false;

        quest.completeQuest(player);
        return true;
    }

    /**
     * Resets the quest and all its progress for the given player.
     *
     * @param name   The name of the quest
     * @param player The player to reset
     * @return True if the quest was reset, false otherwise.
     */
    @Override
    public boolean resetQuest(String name, EntityPlayerMP player) {
        UUID id = getQuestId(name);
        if(id == null) return false;

        // Reset the quest itself...
        Quest quest = Quest.getQuest(id.toString());
        if(quest == null) return false;
        quest.reset(QuestingData.getUserUUID(player));

        // Also reset the completion indicator...
        QuestData questData = QuestingData.getQuestingData(player).getTeam().getQuestData(id.toString());
        if(questData == null) return false;
        questData.completed = false;

        return true;
    }

    /**
     * Used to reset the internal caches for this provider, if any.
     */
    @Override
    public void resetCache() {
        Map<String, Quest> quests = Quest.getQuests();

        for(Map.Entry<String,Quest> quest : quests.entrySet()) {
            questCache.put(quest.getValue().getName(), UUID.fromString(quest.getKey()));
        }
    }

    @Nullable
    private UUID getQuestId(String name) {
        try {
            return questCache.get(name);
        } catch (ExecutionException e) {
            return null;
        }
    }
}
