package org.winterblade.minecraft.harmony.mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.common.TickHandler;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.entities.callbacks.BaseEntityCallback;
import org.winterblade.minecraft.harmony.common.BaseEntityMatcherData;
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
    private static LivingEntityTickHandler<MobShed, MobShed.Handler> shedHandler;
    private static LivingEntityTickHandler<MobPotionEffect, MobPotionEffect.Handler> potionEffectHandler;
    private static LivingEntityTickHandler<IEntityCallbackContainer, BaseEntityCallback.Handler> effectHandler;

    // Queued callbacks; this is a concurrent queue because we may add to it while processing it.
    private static Queue<EntityCallbackData> entityCallbackQueue = new ConcurrentLinkedQueue<>();

    public static void init() {
        inited = true;

        shedHandler = new LivingEntityTickHandler<>(MobShed.Handler.class, CraftingHarmonicsMod.getConfigManager().getShedSeconds());
        potionEffectHandler = new LivingEntityTickHandler<>(MobPotionEffect.Handler.class, CraftingHarmonicsMod.getConfigManager().getPotionEffectTicks());
        effectHandler = new LivingEntityTickHandler<>(BaseEntityCallback.Handler.class, CraftingHarmonicsMod.getConfigManager().getEventTicks());
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


    private static class LivingEntityTickHandler<TMatcher, THandler extends BaseEventMatch.BaseMatchHandler<TMatcher, EntityLivingBase>>
        extends TickHandler<TMatcher, EntityLivingBase, THandler> {

        LivingEntityTickHandler(Class<THandler> handlerClass, int freq) {
            super(freq, handlerClass);
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
                callback.apply(target, new BaseEntityMatcherData(target));
            }
        }
    }
}
