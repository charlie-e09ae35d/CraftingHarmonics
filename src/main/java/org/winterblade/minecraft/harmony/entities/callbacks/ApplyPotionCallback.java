package org.winterblade.minecraft.harmony.entities.callbacks;

import com.google.common.collect.Lists;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "applyPotion")
public class ApplyPotionCallback extends BaseEntityCallback {
    private static WeakHashMap<EntityLivingBase, Map<Potion, HarmonyPotionEffect>> potionHandlers = new WeakHashMap<>();

    /*
     * Serialized properties
     */
    private Potion what;
    private int amplifier;
    private boolean showParticles;
    private int duration;
    private ItemStack[] cures;
    private IEntityCallbackContainer[] onApplied;
    private IEntityCallbackContainer[] onNew;
    private IEntityCallbackContainer[] onExtended;
    private IEntityCallbackContainer[] onExpired;
    private IEntityCallbackContainer[] onRemoved;
    private IEntityCallbackContainer[] onCured;

    /**
     * Called when an effect expires
     * @param entity    The entity it's expiring from
     * @param effect    The effect that's expiring.
     */
    public static void potionExpiredHook(EntityLivingBase entity, PotionEffect effect) {
        // Check if we need to handle for this entity...
        if(!potionHandlers.containsKey(entity)) return;

        // Now check if we need to handle for this effect...
        Map<Potion, HarmonyPotionEffect> effectMap = potionHandlers.get(entity);
        if(!effectMap.containsKey(effect.getPotion())) return;

        // Handle it and clean up...
        effectMap.remove(effect.getPotion()).onRemoved(entity, false);
        if(effectMap.size() <= 0) potionHandlers.remove(entity);
    }

    /**
     * Called when an effect is removed (expired or cured)
     * @param entity    The entity it was removed from
     * @param effect    The effect that was removed.
     */
    public static PotionEffect potionRemovedHook(EntityLivingBase entity, PotionEffect effect) {
        potionCuredHook(entity, effect);
        return effect;
    }

    /**
     * Called when an effect is cured
     * @param entity    The entity it was cured from
     * @param effect    The effect that's been cured.
     */
    public static void potionCuredHook(EntityLivingBase entity, PotionEffect effect) {
        // Check if we need to handle for this entity...
        if(!potionHandlers.containsKey(entity)) return;

        // Now check if we need to handle for this effect...
        Map<Potion, HarmonyPotionEffect> effectMap = potionHandlers.get(entity);
        if(!effectMap.containsKey(effect.getPotion())) return;

        // Handle it and clean up...
        effectMap.remove(effect.getPotion()).onRemoved(entity, true);
        if(effectMap.size() <= 0) potionHandlers.remove(entity);
    }

    public Potion getWhat() {
        return what;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public boolean isShowParticles() {
        return showParticles;
    }

    public int getDuration() {
        return duration;
    }

    public ItemStack[] getCures() {
        return cures != null ? cures : new ItemStack[0];
    }
    
    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        if(what == null) {
            throw new RuntimeException("applyPotion callback has no valid potion.");
        }

        // Default the effect duration...
        if(duration <= 0) {
            duration = CraftingHarmonicsMod.getConfigManager().getPotionEffectTicks()+1;
        }

        if(amplifier < 0) {
            amplifier = 0;
        }

        // And confirm all our callbacks are proper arrays...
        if(onApplied == null) onApplied = new IEntityCallbackContainer[0];
        if(onNew == null) onNew = new IEntityCallbackContainer[0];
        if(onExtended == null) onExtended = new IEntityCallbackContainer[0];
        if(onExpired == null) onExpired = new IEntityCallbackContainer[0];
        if(onRemoved == null) onRemoved = new IEntityCallbackContainer[0];
        if(onCured == null) onCured = new IEntityCallbackContainer[0];
    }

    @Override
    protected void applyTo(Entity target) {
        if(!EntityLivingBase.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not applying potion '{}' to target ({}) as it isn't a mob.", what.getName(), target.getClass().getName());
            return;
        }

        HarmonyPotionEffect effect = new HarmonyPotionEffect(getWhat(), getDuration(),
                getAmplifier(), false, isShowParticles(),
                onExpired != null ? onExpired : new IEntityCallbackContainer[0],
                onCured != null ? onCured : new IEntityCallbackContainer[0],
                onRemoved != null ? onRemoved : new IEntityCallbackContainer[0]);
        effect.setCurativeItems(Lists.newArrayList(getCures()));

        EntityLivingBase entity = (EntityLivingBase)target;
        doApply(entity.getActivePotionEffect(what) == null, entity);

        // Do the effect:
        entity.addPotionEffect(effect);

        registerHandler(entity, what, effect);
    }

    /**
     * Do our apply callbacks
     * @param isNew     If the potion is new or  not
     * @param entity    The entity to apply it to
     */
    private void doApply(boolean isNew, EntityLivingBase entity) {
        if(isNew) {
            runCallbacks(onNew, entity);
        } else {
            runCallbacks(onExtended, entity);
        }

        runCallbacks(onApplied, entity);
    }

    /**
     * Register a potion effect handler to callback to later.
     * @param entity    The entity to apply it to
     * @param what      The potion that was applied
     * @param effect    The effect handler to deal with
     */
    public static void registerHandler(EntityLivingBase entity, Potion what, HarmonyPotionEffect effect) {
        if(!potionHandlers.containsKey(entity)) potionHandlers.put(entity, new HashMap<>());
        potionHandlers.get(entity).put(what, effect);
    }

    /**
     * Custom PotionEffect implementation that handles our cured/expired/removed callbacks.
     */
    public static class HarmonyPotionEffect extends PotionEffect {
        private final IEntityCallbackContainer[] expiredCallbacks;
        private final IEntityCallbackContainer[] curedCallbacks;
        private final IEntityCallbackContainer[] removedCallbacks;
        private boolean expired = false;

        public HarmonyPotionEffect(Potion potionIn, int durationIn, int amplifierIn, boolean ambientIn,
                                   boolean showParticlesIn, IEntityCallbackContainer[] expiredCallbacks,
                                   IEntityCallbackContainer[] curedCallbacks, IEntityCallbackContainer[] removedCallbacks) {
            super(potionIn, durationIn, amplifierIn, ambientIn, showParticlesIn);
            this.expiredCallbacks = expiredCallbacks;
            this.curedCallbacks = curedCallbacks;
            this.removedCallbacks = removedCallbacks;
        }

        public void onRemoved(EntityLivingBase entity, boolean wasCured) {
            // Only call our update on the server...
            if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT || expired) return;

            if(wasCured) {
                MobTickRegistry.addCallbackSet(entity, curedCallbacks);
            } else {
                MobTickRegistry.addCallbackSet(entity, expiredCallbacks);
            }

            MobTickRegistry.addCallbackSet(entity, removedCallbacks);
        }
    }
}
