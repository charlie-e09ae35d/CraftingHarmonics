package org.winterblade.minecraft.harmony.mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.entities.callbacks.EntityCallbackContainer;
import org.winterblade.minecraft.harmony.entities.effects.MobPotionEffect;
import org.winterblade.minecraft.harmony.mobs.sheds.MobShed;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Matt on 5/10/2016.
 */
public class MobTickRegistry {
    private MobTickRegistry() {}

    private static boolean inited = false;
    private static boolean isActive = false;

    // Tick handlers
    private static TickHandler<MobShed, MobShed.Handler> shedHandler;
    private static TickHandler<MobPotionEffect, MobPotionEffect.Handler> potionEffectHandler;
    private static TickHandler<IEntityCallbackContainer, EntityCallbackContainer.Handler> effectHandler;

    // Queued callbacks; this is a concurrent queue because we may add to it while processing it.
    private static Queue<EntityCallbackData> entityCallbackQueue = new ConcurrentLinkedQueue<>();

    public static void init() {
        inited = true;

        shedHandler = new TickHandler<>(MobShed.Handler.class, CraftingHarmonicsMod.getConfigManager().getShedSeconds());
        potionEffectHandler = new TickHandler<>(MobPotionEffect.Handler.class, CraftingHarmonicsMod.getConfigManager().getPotionEffectTicks());
        effectHandler = new TickHandler<>(EntityCallbackContainer.Handler.class, CraftingHarmonicsMod.getConfigManager().getEventTicks());
    }

    /**
     * Handles a mob shed event
     * @param evt    The event to process
     */
    public static void handleTick(TickEvent.WorldTickEvent evt) {
        // Init, if we have to...
        if(!inited) init();
        if(!isActive) return;

        // Figure out what we're doing...
        boolean shedsActive = shedHandler.isActiveThisTick(evt);
        boolean potionsActive = potionEffectHandler.isActiveThisTick(evt);
        boolean effectsActive = effectHandler.isActiveThisTick(evt);

        // If we're not doing anything...
        if(!shedsActive && !potionsActive && !effectsActive) return;

        Random rand = evt.world.rand;
        // Doing it this way to avoid doing modulo for potentially thousands of entities in a LivingUpdate event.
        List<EntityLivingBase> entities = evt.world.getEntities(EntityLivingBase.class, EntitySelectors.IS_ALIVE);

        for(EntityLivingBase entity : entities) {
            String entityName = entity.getName();
            String entityClassName = entity.getClass().getName();

            if(shedsActive) shedHandler.handle(rand, entity, entityName, entityClassName);
            if(potionsActive) potionEffectHandler.handle(rand, entity, entityName, entityClassName);
            if(effectsActive) effectHandler.handle(rand, entity, entityName, entityClassName);
        }
    }

    /**
     * Add a set of callbacks to the callback queue
     * @param target        The target of the operation
     * @param callbacks     The callbacks
     */
    public static void addCallbackSet(Entity target, IEntityCallbackContainer[] callbacks) {
        entityCallbackQueue.add(new EntityCallbackData(target, callbacks));
    }

    /**
     * Process the current callback queue
     */
    public static void processCallbackQueue() {
        for (Iterator<EntityCallbackData> iterator = entityCallbackQueue.iterator(); iterator.hasNext(); ) {
            EntityCallbackData callbackData = iterator.next();
            iterator.remove();

            try {
                callbackData.runCallbacks();
            } catch (Exception ex) {
                LogHelper.error("Error processing entity callbacks.", ex);
            }

            // TODO: Make this have a configurable limiter on the number of callbacks
        }
    }

    /**
     * Updates if we're actually active overall right now...
     */
    private static void calcActive() {
        isActive = shedHandler.isActive() || potionEffectHandler.isActive() || effectHandler.isActive();
    }

    /**
     * Shed registrations
     */

    @Nullable
    public static UUID registerShed(String[] what, MobShed[] drops) {
        // Init, if we have to...
        if(!inited) init();

        return shedHandler.registerHandler(what, drops);
    }

    public static void applyShed(UUID ticket) {
        shedHandler.apply(ticket);
        calcActive();
    }

    public static void removeShed(UUID ticket) {
        shedHandler.remove(ticket);
        calcActive();
    }

    /**
     * Potion registrations
     */

    @Nullable
    public static UUID registerPotionEffect(String[] what, MobPotionEffect[] effects) {
        // Init, if we have to...
        if(!inited) init();

        return potionEffectHandler.registerHandler(what, effects);
    }

    public static void applyPotionEffect(UUID ticket) {
        potionEffectHandler.apply(ticket);
        calcActive();
    }

    public static void removePotionEffect(UUID ticket) {
        potionEffectHandler.remove(ticket);
        calcActive();
    }

    /*
     * Entity Effects
     */

    public static UUID registerEntityEffects(String[] what, IEntityCallbackContainer[] effects) {
        if(!inited) init();
        return effectHandler.registerHandler(what, effects);
    }

    public static void applyEntityEffects(UUID ticket) {
        effectHandler.apply(ticket);
        calcActive();
    }

    public static void removeEntityEffects(UUID ticket) {
        effectHandler.remove(ticket);
        calcActive();
    }


    /**
     * Tick handler
     */

    private static class TickHandler<TMatcher, THandler extends BaseEventMatch.BaseMatchHandler<TMatcher>> {
        private final Class<THandler> handlerClass;
        private final int freq;
        private boolean isActive = false;

        // Full handler list
        private final Map<UUID, THandler> handlers = new HashMap<>();

        // Entity-to-handler cache
        private final Map<String, Set<UUID>> cache = new HashMap<>();

        // Active handlers from all sets
        private final Set<UUID> activeHandlers = new LinkedHashSet<>();

        TickHandler(Class<THandler> handlerClass, int freq) {
            this.handlerClass = handlerClass;
            this.freq = freq;
        }

        void handle(Random rand, EntityLivingBase entity, String entityName, String entityClassName) {
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
        UUID registerHandler(String[] what, TMatcher[] matchers) {
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
        }

        public void remove(UUID ticket) {
            activeHandlers.remove(ticket);
            if(activeHandlers.size() <= 0) isActive = false;
        }

        boolean isActive() {
            return isActive;
        }

        boolean isActiveThisTick(TickEvent.WorldTickEvent evt) {
            return isActive() && (evt.world.getTotalWorldTime() % freq) == 0;
        }
    }

    private static class EntityCallbackData {
        private final WeakReference<Entity> targetRef;
        private final IEntityCallbackContainer[] callbacks;

        EntityCallbackData(Entity target, IEntityCallbackContainer[] callbacks) {
            targetRef = new WeakReference<>(target);
            this.callbacks = callbacks;
        }

        public void runCallbacks() {
            Entity target = targetRef.get();
            if(target == null) return;

            for(IEntityCallbackContainer callback : callbacks) {
                callback.apply(target);
            }
        }
    }
}
