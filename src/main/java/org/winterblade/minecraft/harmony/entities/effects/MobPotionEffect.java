package org.winterblade.minecraft.harmony.entities.effects;

import com.google.common.collect.Lists;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.entities.callbacks.ApplyPotionCallback;
import org.winterblade.minecraft.harmony.entities.callbacks.EntityCallbackContainer;
import org.winterblade.minecraft.harmony.scripting.DeserializerHelpers;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseMatchingDeserializer;
import org.winterblade.minecraft.harmony.scripting.deserializers.ItemStackDeserializer;
import org.winterblade.minecraft.harmony.scripting.deserializers.PotionDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.Random;

/**
 * Created by Matt on 5/20/2016.
 */
public class MobPotionEffect extends BaseEventMatch<Entity, IEntityMatcherData, IEntityMatcher> {
    /*
     * Serialized properties
     */
    private Potion what;
    private int amplifier;
    private boolean showParticles;
    private int duration;
    private ItemStack[] cures;
    private IEntityCallbackContainer[] applyCallbacks;
    private IEntityCallbackContainer[] newCallbacks;
    private IEntityCallbackContainer[] extendedCallbacks;
    private IEntityCallbackContainer[] expiredCallbacks;
    private IEntityCallbackContainer[] removedCallbacks;
    private IEntityCallbackContainer[] curedCallbacks;

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
            for(IEntityCallbackContainer callback : newCallbacks) {
                callback.apply(entity);
            }
        } else if(!isNew && extendedCallbacks != null){
            for(IEntityCallbackContainer callback : extendedCallbacks) {
                callback.apply(entity);
            }
        }

        if(applyCallbacks != null) {
            for(IEntityCallbackContainer callback : applyCallbacks) {
                callback.apply(entity);
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
                ApplyPotionCallback.HarmonyPotionEffect effect;
                BaseMatchResult result;

                do {
                    effect = new ApplyPotionCallback.HarmonyPotionEffect(matcher.getWhat(), matcher.getDuration(),
                            matcher.getAmplifier(), false, matcher.isShowParticles(),
                            matcher.expiredCallbacks != null ? matcher.expiredCallbacks : new IEntityCallbackContainer[0],
                            matcher.curedCallbacks != null ? matcher.curedCallbacks : new IEntityCallbackContainer[0],
                            matcher.removedCallbacks != null ? matcher.removedCallbacks : new IEntityCallbackContainer[0]);
                    effect.setCurativeItems(Lists.newArrayList(matcher.getCures()));

                    // Check if this drop matches:
                    result = matcher.matches(entity, new BaseEntityMatcherData());
                    if(result.isMatch()) break;
                    matcher = (MobPotionEffect) matcher.getAltMatch();
                } while(matcher != null);

                if(!result.isMatch()) continue;

                // Make sure we have sane drop amounts:
                if (effect.getDuration() < 0) continue;

                // Now perform our updates:
                if(result.getCallback() != null) result.getCallback().run();

                // Apply our callbacks
                matcher.doApply(entity.getActivePotionEffect(matcher.getWhat()) == null, entity);

                // Do the effect:
                entity.addPotionEffect(effect);

                ApplyPotionCallback.registerHandler(entity, matcher.getWhat(), effect);
            }
        }
    }

    @ScriptObjectDeserializer(deserializes = MobPotionEffect.class)
    public static class Deserializer extends BaseMatchingDeserializer<Entity, IEntityMatcherData, IEntityMatcher, MobPotionEffect> {
        private static final PotionDeserializer POTION_DESERIALIZER = new PotionDeserializer();
        private static final ItemStackDeserializer ITEM_STACK_DESERIALIZER = new ItemStackDeserializer();
        private static final EntityCallbackContainer.Deserializer ENTITY_CALLBACK_DESERIALIZER = new EntityCallbackContainer.Deserializer();

        public Deserializer() {super(IEntityMatcher.class);}

        @Override
        protected MobPotionEffect newInstance(String type) {
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
            output.cures = DeserializerHelpers.convertArrayWithDeserializer(mirror, "cures", ITEM_STACK_DESERIALIZER, ItemStack.class);
            output.newCallbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "onNew", ENTITY_CALLBACK_DESERIALIZER, IEntityCallbackContainer.class);
            output.extendedCallbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "onExtended", ENTITY_CALLBACK_DESERIALIZER, IEntityCallbackContainer.class);
            output.applyCallbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "onApplied", ENTITY_CALLBACK_DESERIALIZER, IEntityCallbackContainer.class);
            output.expiredCallbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "onExpired", ENTITY_CALLBACK_DESERIALIZER, IEntityCallbackContainer.class);
            output.curedCallbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "onCured", ENTITY_CALLBACK_DESERIALIZER, IEntityCallbackContainer.class);
            output.removedCallbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "onRemoved", ENTITY_CALLBACK_DESERIALIZER, IEntityCallbackContainer.class);
        }
    }

}
