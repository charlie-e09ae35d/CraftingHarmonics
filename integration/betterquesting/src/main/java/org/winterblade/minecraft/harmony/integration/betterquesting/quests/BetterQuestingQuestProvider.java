package org.winterblade.minecraft.harmony.integration.betterquesting.quests;

import betterquesting.lives.LifeManager;
import betterquesting.party.PartyInstance;
import betterquesting.party.PartyManager;
import betterquesting.quests.QuestDatabase;
import betterquesting.quests.QuestInstance;
import betterquesting.quests.tasks.TaskBase;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.entity.player.EntityPlayerMP;
import org.winterblade.minecraft.harmony.api.questing.IQuestProvider;
import org.winterblade.minecraft.harmony.api.questing.QuestProvider;
import org.winterblade.minecraft.harmony.api.questing.QuestStatus;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Created by Matt on 5/29/2016.
 */
@QuestProvider
public class BetterQuestingQuestProvider implements IQuestProvider {
    private static LoadingCache<String, Integer> questCache = CacheBuilder.newBuilder().build(
            // If we haven't loaded it in the pre-load, it doesn't exist.
            new CacheLoader<String, Integer>() {
                @Override
                public Integer load(String key) throws Exception {
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
        QuestInstance questInstance = getQuestByName(name);
        if(questInstance == null) return QuestStatus.INVALID;

        boolean isComplete = questInstance.isComplete(player.getPersistentID());

        if(isComplete && questInstance.HasClaimed(player.getPersistentID())) return QuestStatus.CLOSED;
        if(isComplete) return QuestStatus.COMPLETE;
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
        QuestInstance questInstance = getQuestByName(name);
        if(questInstance == null) return false;

        List<UUID> playerIds = getAllInvolvedPlayerIds(player);

        // Complete all the tasks!
        for(TaskBase task : questInstance.tasks) {
            for(UUID id : playerIds) {
                task.setCompletion(id, true);
            }
        }

        // And actually complete the quest as well...
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
        QuestInstance questInstance = getQuestByName(name);
        if(questInstance == null) return false;

        // Check if we're in a party...
        List<UUID> uuids = getAllInvolvedPlayerIds(player);

        for(UUID id : uuids) {
            questInstance.ResetProgress(id);
            questInstance.RemoveUserEntry(id);
        }

        questInstance.UpdateClients();

        return true;
    }

    /**
     * Used to reset the internal caches for this provider, if any.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void resetCache() {
        try {
            // If trying to access this normally, you get a NoSuchFieldError.  Reflectively?  Oh, it works fine.
            ConcurrentHashMap<Integer, QuestInstance> questDB = (ConcurrentHashMap<Integer, QuestInstance>) QuestDatabase.class.getField("questDB").get(null);
            for (Map.Entry<Integer, QuestInstance> quest : questDB.entrySet()) {
                questCache.put(quest.getValue().name, quest.getValue().questID);
            }
        } catch(Exception | NoSuchFieldError e) {
            // What?
            LogHelper.warn("Something went terribly wrong.", e);
        }
    }

    /**
     * Gets the quest name from our cache by name
     * @param name    The name of the quest to get
     * @return        The quest, or null if it wasn't found.
     */
    @Nullable
    private QuestInstance getQuestByName(String name) {
        int questId;

        try {
            questId = questCache.get(name);
        } catch (ExecutionException e) {
            return null;
        }

        if(questId < 0) return null;

        return QuestDatabase.getQuestByID(questId);
    }

    /**
     * Gets a list of all involved player IDs for the given quest
     * @param player    The player to get
     * @return          A list of player IDs; either from the party or just the player.
     */
    private List<UUID> getAllInvolvedPlayerIds(EntityPlayerMP player) {
        PartyInstance party = PartyManager.GetParty(player.getPersistentID());

        // Figure out what we need to work on...
        return party != null
                ? party.GetMembers().stream().map(p -> p.userID).collect(Collectors.toList())
                : Collections.singletonList(player.getPersistentID());
    }

}
