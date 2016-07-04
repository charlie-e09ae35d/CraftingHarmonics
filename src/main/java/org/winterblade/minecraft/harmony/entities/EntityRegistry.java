package org.winterblade.minecraft.harmony.entities;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.common.dto.NameClassPair;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.*;
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

}
