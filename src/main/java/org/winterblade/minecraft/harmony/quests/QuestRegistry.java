package org.winterblade.minecraft.harmony.quests;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Loader;
import org.winterblade.minecraft.harmony.api.questing.IQuestProvider;
import org.winterblade.minecraft.harmony.api.questing.QuestStatus;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 5/30/2016.
 */
public class QuestRegistry implements IQuestProvider {
    private QuestRegistry() {}

    public static final QuestRegistry instance = new QuestRegistry();
    private static final Map<String, IQuestProvider> providers = new HashMap<>();

    /**
     * Does initial registration of the quest providers
     * @param registeredClasses    The registered classes.
     */
    public static void registerProviders(Map<String, Class<IQuestProvider>> registeredClasses) {

        providers.clear();

        for(Map.Entry<String, Class<IQuestProvider>> regClass : registeredClasses.entrySet()) {
            try {
                IQuestProvider provider = regClass.getValue().newInstance();

                if(provider.getDependencyList() != null
                        && !Arrays.stream(provider.getDependencyList()).allMatch(Loader::isModLoaded)) {
                    LogHelper.info("Not registering quest provider '{}', due to a missing dependency.", provider.getName());
                    continue;
                }

                providers.put(provider.getName(), provider);
                LogHelper.info("Registering quest provider '{}'.", provider.getName());
            } catch (InstantiationException | IllegalAccessException | NoClassDefFoundError e) {
                LogHelper.warn("Error registering quest provider '{}'.", regClass.getKey(), e);
            }
        }
    }

    /**
     * Gets the name of this provider.
     *
     * @return The name of the provider, used to reference it in matchers/callbacks.
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Get a list of mod dependencies for this provider
     *
     * @return A string array of mod dependencies.
     */
    @Override
    public String[] getDependencyList() {
        return new String[0];
    }

    /**
     * Gets if hardcore mode is enabled for the given provider.
     *
     * @return True if it is, false otherwise.  Should return false if the underlying provider doesn't support hardcore.
     */
    @Override
    public boolean isHardcoreModeEnabled() {
        return providers.values().stream().anyMatch(IQuestProvider::isHardcoreModeEnabled);
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
        if(player == null) return QuestStatus.INVALID;

        // Return the first not-invalid status...
        for(IQuestProvider provider : providers.values()) {
            QuestStatus questStatus = provider.getQuestStatus(name, player);
            if(questStatus != QuestStatus.INVALID) return questStatus;
        }
        return QuestStatus.INVALID;
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
        return providers.values().stream().anyMatch(p -> p.inParty(player));
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
        return providers.values().stream().anyMatch(p -> p.hasSharedLives(player));
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
        return providers.values().stream().anyMatch(p -> p.hasSharedLoot(player));
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
        int livesGiven = 0;
        for(IQuestProvider provider : providers.values()) {
            livesGiven += provider.giveLives(player, lives);
        }
        return livesGiven;
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
        int livesTaken = 0;
        for(IQuestProvider provider : providers.values()) {
            livesTaken += provider.takeLives(player, lives);
        }
        return livesTaken;
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
        return providers.values().stream().anyMatch(p -> p.completeQuest(name, player));
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
        return providers.values().stream().anyMatch(p -> p.resetQuest(name, player));
    }

    /**
     * Used to reset the internal caches for this provider, if any.
     */
    @Override
    public void resetCache() {
        providers.values().forEach(IQuestProvider::resetCache);
    }
}
