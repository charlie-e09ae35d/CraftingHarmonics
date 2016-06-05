package org.winterblade.minecraft.harmony.common;

import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Tick handler
 */
public class TickHandler<TMatcher, TSource, THandler extends BaseEventMatch.BaseMatchHandler<TMatcher, TSource>> {
    private final Class<THandler> handlerClass;
    private final int freq;

    // Full handler list
    private final Map<UUID, THandler> handlers = new HashMap<>();

    // Entity-to-handler cache
    private final Map<String, Set<UUID>> cache = new HashMap<>();

    // Active handlers from all sets
    private final Set<UUID> activeHandlers = new LinkedHashSet<>();
    private boolean isActive = false;

    protected TickHandler(int freq, Class<THandler> handlerClass) {
        this.freq = freq;
        this.handlerClass = handlerClass;
    }

    public void handle(Random rand, TSource entity, String entityName, String entityClassName) {
        // If we have a cached entry, just do that...
        if(cache.containsKey(entityClassName)) {
            for(UUID id : cache.get(entityClassName)) {
                // If we're inactive, just pass on it
                if(!activeHandlers.contains(id)) continue;

                // Get the handler, make sure we actually got one, then apply it
                THandler handler = handlers.get(id);
                if(handler == null) continue;
                handler.apply(rand, entity);
            }
            return;
        }

        // Populate the cache
        cache.put(entityClassName, new HashSet<>());

        for (Map.Entry<UUID, THandler> entry : handlers.entrySet()) {
            // If we don't have a handler, or the handler doesn't match:
            if (!entry.getValue().isMatch(entityName) && !entry.getValue().isMatch(entityClassName)) continue;
            cache.get(entityClassName).add(entry.getKey());

            // If it's active, apply it
            if(activeHandlers.contains(entry.getKey())) entry.getValue().apply(rand, entity);
        }
    }

    /**
     * Registers the given handler for this handler
     * @param what        The mobs to match
     * @param matchers    The additional matchers to use
     * @return            The registered UUID
     */
    @Nullable
    public UUID registerHandler(String[] what, TMatcher[] matchers) {
        UUID id = UUID.randomUUID();
        THandler handler;
        try {
            handler = handlerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LogHelper.error("Error registering tick handler.", e);
            return null;
        }

        // Set our matchers
        handler.setWhat(what);
        handler.setMatchers(matchers);

        handlers.put(id, handler);
        cache.clear();
        return id;
    }

    public void apply(UUID ticket) {
        isActive = true;
        activeHandlers.add(ticket);
        cache.clear();
    }

    public void remove(UUID ticket) {
        activeHandlers.remove(ticket);
        if(activeHandlers.size() <= 0) isActive = false;
        cache.clear();
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isActiveThisTick(TickEvent.WorldTickEvent evt) {
        return isActive() && (evt.world.getTotalWorldTime() % freq) == 0;
    }
}
