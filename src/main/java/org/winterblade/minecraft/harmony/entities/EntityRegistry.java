package org.winterblade.minecraft.harmony.entities;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.common.dto.NameClassPair;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Matt on 7/3/2016.
 */
public class EntityRegistry {
    private static final Map<UUID, EntityInteractionHandler> handlers = new HashMap<>();
    private static final Set<UUID> activeHandlers = new LinkedHashSet<>();
    private static final LoadingCache<NameClassPair, Set<UUID>> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .build(new CacheLoader<NameClassPair, Set<UUID>>() {
                @Override
                public Set<UUID> load(NameClassPair key) throws Exception {
                    if(CraftingHarmonicsMod.getConfigManager().debugMobDropEvents()) {
                        LogHelper.info("Generating entity interaction handler cache for '{}'.", key.toString());
                    }

                    return activeHandlers.stream().filter(id -> {
                        EntityInteractionHandler handler = handlers.get(id);
                        return handler != null && handler.isMatch(key);
                    }).collect(Collectors.toSet());
                }
            });
    private EntityRegistry() {}

    /**
     * Checks the given entity against the active handlers
     * @param target    The target entity
     * @param source    The source entity
     * @return          True if the interaction should go through, false otherwise
     */
    public static boolean allowInteractionBetween(Entity target, Entity source) {
        Set<UUID> ids;

        // Attempt to query the cache for this target entity.
        try {
            ids = cache.get(new NameClassPair(target.getName(), target.getClass()));
        } catch (ExecutionException e) {
            LogHelper.warn("Unable to get interaction handlers for interaction between {} and {}",
                    source.getName(), target.getName());
            return true;
        }

        // Iterate our handlers
        for (UUID id : ids) {
            EntityInteractionHandler handler = handlers.get(id);
            if(handler == null) continue;

            // The first one to deny wins:
            if(!handler.check(target, source)) return false;
        }

        return true;
    }
}
