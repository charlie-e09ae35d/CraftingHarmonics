package org.winterblade.minecraft.harmony.mobs.effects;

import com.google.common.collect.Lists;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.IEntityCallback;
import org.winterblade.minecraft.harmony.api.mobs.effects.IMobPotionEffectMatcher;
import org.winterblade.minecraft.harmony.callbacks.mobs.EntityCallbackDeserializer;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseMatchingDeserializer;
import org.winterblade.minecraft.harmony.scripting.deserializers.ItemStackDeserializer;
import org.winterblade.minecraft.harmony.scripting.deserializers.PotionDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.Random;

/**
 * Created by Matt on 5/20/2016.
 */
public class MobPotionEffect extends BaseEventMatch<EntityLivingBase, PotionEffect, IMobPotionEffectMatcher> {
    /*
     * Serialized properties
     */
    private Potion what;
    private int amplifier;
    private boolean showParticles;
    private int duration;
    private ItemStack[] cures;
    private IEntityCallback[] applyCallbacks;
    private IEntityCallback[] newCallbacks;
    private IEntityCallback[] extendedCallbacks;
    private IEntityCallback[] expiredCallbacks;

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

    public void doApply(boolean isNew, EntityLivingBase entity) {
        if(isNew && newCallbacks != null) {
            for(IEntityCallback callback : newCallbacks) {
                callback.apply(entity, entity.getEntityWorld());
            }
        } else if(!isNew && extendedCallbacks != null){
            for(IEntityCallback callback : extendedCallbacks) {
                callback.apply(entity, entity.getEntityWorld());
            }
        }

        if(applyCallbacks != null) {
            for(IEntityCallback callback : applyCallbacks) {
                callback.apply(entity, entity.getEntityWorld());
            }
        }
    }

    public static class Handler extends BaseEventMatch.BaseMatchHandler<MobPotionEffect> {
        /**
         * Applies the handler to the given mob
         * @param rand       The rand to use
         * @param entity     The entity to apply to
         */
        public void apply(Random rand, EntityLivingBase entity) {
            // Now, actually calculate out our drop rates...
            for (MobPotionEffect matcher : this.getMatchers()) {
                // Wrap this in our class, so we can callback when it's expired
                PotionEffect effect = new PotionEffectWithExpiredCallback(matcher.getWhat(), matcher.getDuration(),
                        matcher.getAmplifier(), false, matcher.isShowParticles(),
                        matcher.expiredCallbacks != null ? matcher.expiredCallbacks : new IEntityCallback[0]);
                effect.setCurativeItems(Lists.newArrayList(matcher.getCures()));

                // Check if this drop matches:
                BaseMatchResult result = matcher.matches(entity, effect);
                if(!result.isMatch()) continue;

                // Make sure we have sane drop amounts:
                if (effect.getDuration() < 0) continue;

                // Now perform our updates:
                if(result.getCallback() != null) result.getCallback().run();

                // Apply our callbacks
                matcher.doApply(entity.getActivePotionEffect(matcher.getWhat()) != null, entity);

                // Do the effect:
                entity.addPotionEffect(effect);
            }
        }
    }

    @ScriptObjectDeserializer(deserializes = MobPotionEffect.class)
    public static class Deserializer extends BaseMatchingDeserializer<EntityLivingBase, PotionEffect, IMobPotionEffectMatcher, MobPotionEffect> {
        private static final PotionDeserializer POTION_DESERIALIZER = new PotionDeserializer();
        private static final ItemStackDeserializer ITEM_STACK_DESERIALIZER = new ItemStackDeserializer();
        private static final EntityCallbackDeserializer ENTITY_CALLBACK_DESERIALIZER = new EntityCallbackDeserializer();

        public Deserializer() {super(IMobPotionEffectMatcher.class);}

        @Override
        protected MobPotionEffect newInstance() {
            return new MobPotionEffect();
        }

        @Override
        protected void update(ScriptObjectMirror mirror, MobPotionEffect output) {
            // Our base potion
            output.what = (Potion) POTION_DESERIALIZER.Deserialize(mirror.get("what"));

            // Duration; defaults to the number of ticks between checks
            output.duration = mirror.containsKey("duration") ? (int) ScriptUtils.convert(mirror.get("duration"), Integer.class)
                    : CraftingHarmonicsMod.getConfigManager().getPotionEffectTicks()+1;

            output.amplifier = mirror.containsKey("amplifier") ? (int) ScriptUtils.convert(mirror.get("amplifier"), Integer.class) : 0;
            output.showParticles = mirror.containsKey("showParticles") && (boolean) ScriptUtils.convert(mirror.get("showParticles"), Boolean.class);
            output.cures = convertArrayWithDeserializer(mirror, "cures", ITEM_STACK_DESERIALIZER, ItemStack.class);
            output.newCallbacks = convertArrayWithDeserializer(mirror, "onNew", ENTITY_CALLBACK_DESERIALIZER, IEntityCallback.class);
            output.extendedCallbacks = convertArrayWithDeserializer(mirror, "onExtended", ENTITY_CALLBACK_DESERIALIZER, IEntityCallback.class);
            output.applyCallbacks = convertArrayWithDeserializer(mirror, "onApplied", ENTITY_CALLBACK_DESERIALIZER, IEntityCallback.class);
            output.expiredCallbacks = convertArrayWithDeserializer(mirror, "onExpired", ENTITY_CALLBACK_DESERIALIZER, IEntityCallback.class);
        }
    }

    private static class PotionEffectWithExpiredCallback extends PotionEffect {

        private final IEntityCallback[] expiredCallbacks;

        public PotionEffectWithExpiredCallback(Potion potionIn, int durationIn, int amplifierIn, boolean ambientIn, boolean showParticlesIn, IEntityCallback[] expiredCallbacks) {
            super(potionIn, durationIn, amplifierIn, ambientIn, showParticlesIn);
            this.expiredCallbacks = expiredCallbacks;
        }

        @Override
        public boolean onUpdate(EntityLivingBase entityIn) {
            if(super.onUpdate(entityIn)) return true;

            // If we're expired, run our callbacks:
            for(IEntityCallback callback : expiredCallbacks) {
                callback.apply(entityIn, entityIn.getEntityWorld());
            }

            return false;
        }
    }
}
