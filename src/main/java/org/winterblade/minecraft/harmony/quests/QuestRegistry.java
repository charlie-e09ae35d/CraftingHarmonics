package org.winterblade.minecraft.harmony.quests;

import com.google.common.cache.CacheLoader;
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
        return providers.values().stream().anyMatch((provider) -> {
            try {
                return provider.isHardcoreModeEnabled();
            } catch (Exception e) {
                LogHelper.warn("Unable to check if hardcore mode is on for provider '{}'.", provider.getName(), e);
                return false;
            }
        });
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
            try {
                QuestStatus questStatus = provider.getQuestStatus(name, player);
                if(questStatus != QuestStatus.INVALID) return questStatus;
            } catch (CacheLoader.InvalidCacheLoadException e) {
                LogHelper.warn("Provider '{}' has no quest named '{}'; run /ch reloadQuestCache if you just added it.", provider.getName(), name);
            } catch (Exception e) {
                LogHelper.warn("Unable to read quest '{}' from provider '{}'.", name, provider.getName());
            }
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
        return providers.values().stream().anyMatch(p -> {
            try {
                return p.inParty(player);
            } catch (Exception e) {
                LogHelper.warn("Error checking if {} is in a party in provider '{}'.", player.getName(), p.getName(), e);
                return false;
            }
        });
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
        return providers.values().stream().anyMatch(p -> {
            try {
                return p.hasSharedLives(player);
            } catch (Exception e) {
                LogHelper.warn("Error checking if {} is using shared lives in provider '{}'.", player.getName(), p.getName(), e);
                return false;
            }
        });
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
        return providers.values().stream().anyMatch(p -> {
            try {
                return p.hasSharedLoot(player);
            } catch (Exception e) {
                LogHelper.warn("Error checking if {} is using shared loot in provider '{}'.", player.getName(), p.getName(), e);
                return false;
            }
        });
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
            try {
                livesGiven += provider.giveLives(player, lives);
            } catch (Exception e) {
                LogHelper.warn("Unable to give lives in provider '{}'", provider.getName(), e);
            }
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
            try {
                livesTaken += provider.takeLives(player, lives);
            }
            catch (Exception e) {
                LogHelper.warn("Unable to take lives in provider '{}'", provider.getName(), e);
            }
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
        return providers.values().stream().anyMatch(p -> {
            try {
                return p.completeQuest(name, player);
            } catch (CacheLoader.InvalidCacheLoadException e) {
                LogHelper.warn("Provider '{}' has no quest named '{}'; run /ch reloadQuestCache if you just added it.", p.getName(), name);
                return false;
            } catch (Exception e) {
                LogHelper.warn("Unable to complete quest '{}' in provider '{}'", name, p.getName(), e);
                return false;
            }
        });
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
        return providers.values().stream().anyMatch(p -> {
            try {
                return p.resetQuest(name, player);
            } catch (CacheLoader.InvalidCacheLoadException e) {
                LogHelper.warn("Provider '{}' has no quest named '{}'; run /ch reloadQuestCache if you just added it.", p.getName(), name);
                return false;
            } catch (Exception e) {
                LogHelper.warn("Unable to reset quest '{}' in provider '{}'", name, p.getName(), e);
                return false;
            }
        });
    }

    /**
     * Used to reset the internal caches for this provider, if any.
     */
    @Override
    public void resetCache() {
        providers.values().forEach((provider) -> {
            try {
                provider.resetCache();
            } catch (Exception e) {
                LogHelper.warn("Error resetting the quest cache for '{}'.", provider.getName(), e);
            }
        });
    }
}
