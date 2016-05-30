package com.winterblade.minecraft.harmony.integration.betterquesting.quests;

import betterquesting.lives.LifeManager;
import betterquesting.party.PartyInstance;
import betterquesting.party.PartyManager;
import betterquesting.quests.QuestDatabase;
import betterquesting.quests.QuestInstance;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.entity.player.EntityPlayerMP;
import org.winterblade.minecraft.harmony.api.questing.IQuestProvider;
import org.winterblade.minecraft.harmony.api.questing.QuestProvider;
import org.winterblade.minecraft.harmony.api.questing.QuestStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Created by Matt on 5/29/2016.
 */
@QuestProvider
public class BetterQuestingQuestProvider implements IQuestProvider {
    private static LoadingCache<String, QuestInstance> questCache = CacheBuilder.newBuilder().build(
            // If we haven't loaded it in the pre-load, it doesn't exist.
            new CacheLoader<String, QuestInstance>() {
                @Override
                public QuestInstance load(String key) throws Exception {
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
        return "bq";
    }

    /**
     * Get a list of mod dependencies for this provider
     *
     * @return A string array of mod dependencies.
     */
    @Override
    public String[] getDependencyList() {
        return new String[] {"betterquesting"};
    }

    /**
     * Gets if hardcore mode is enabled for the given provider.
     *
     * @return True if it is, false otherwise.  Should return false if the underlying provider doesn't support hardcore.
     */
    @Override
    public boolean isHardcoreModeEnabled() {
        return QuestDatabase.bqHardcore;
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
        QuestInstance questInstance;

        try {
            questInstance = questCache.get(name);
        } catch (ExecutionException e) {
            return QuestStatus.INVALID;
        }

        if(questInstance == null) return QuestStatus.INVALID;

        if(questInstance.HasClaimed(player.getPersistentID())) return QuestStatus.CLOSED;
        if(questInstance.isComplete(player.getPersistentID())) return QuestStatus.COMPLETE;
        if(questInstance.isUnlocked(player.getPersistentID())) return QuestStatus.ACTIVE;

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
        return PartyManager.GetParty(player.getPersistentID()) != null;
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
        if (!isHardcoreModeEnabled()) return false;
        PartyInstance party = PartyManager.GetParty(player.getPersistentID());
        return party != null && party.lifeShare;
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
        PartyInstance party = PartyManager.GetParty(player.getPersistentID());
        return party != null && party.lootShare;
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
        int curLives = LifeManager.getLives(player);
        LifeManager.AddRemoveLives(player, lives);
        return LifeManager.getLives(player) - curLives;
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
        int curLives = LifeManager.getLives(player);
        LifeManager.AddRemoveLives(player, 0 - lives);
        return curLives - LifeManager.getLives(player);
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
        QuestInstance questInstance;

        try {
            questInstance = questCache.get(name);
        } catch (ExecutionException e) {
            return false;
        }

        questInstance.setComplete(player.getPersistentID(), player.getEntityWorld().getTotalWorldTime());
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
        QuestInstance questInstance;

        try {
            questInstance = questCache.get(name);
        } catch (ExecutionException e) {
            return false;
        }

        // Check if we're in a party...
        PartyInstance party = PartyManager.GetParty(player.getPersistentID());

        // Figure out what we need to work on...
        List<UUID> uuids = party != null
                ? party.GetMembers().stream().map(p -> p.userID).collect(Collectors.toList())
                : Collections.singletonList(player.getPersistentID());

        for(UUID id : uuids) {
            questInstance.ResetProgress(id);
        }

        return true;
    }

    /**
     * Used to reset the internal caches for this provider, if any.
     */
    @Override
    public void resetCache() {
        for(Map.Entry<Integer, QuestInstance> quest : QuestDatabase.questDB.entrySet()) {
            questCache.put(quest.getValue().name, quest.getValue());
        }
    }
}
