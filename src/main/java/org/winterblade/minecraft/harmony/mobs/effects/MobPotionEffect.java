package org.winterblade.minecraft.harmony.mobs.effects;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.effects.IMobPotionEffectMatcher;

import java.util.Random;

/**
 * Created by Matt on 5/20/2016.
 */
public class MobPotionEffect extends BaseEventMatch<EntityLiving, PotionEffect, IMobPotionEffectMatcher> {
    /*
     * Serialized properties
     */
    private Potion what;
    private int amplifier;
    private boolean showParticles;
    private int duration;
    private ItemStack[] cures;

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

    public static class Handler extends BaseEventMatch.BaseMatchHandler<MobPotionEffect> {
        /**
         * Applies the handler to the given mob
         * @param rand       The rand to use
         * @param entity     The entity to apply to
         */
        public void apply(Random rand, EntityLiving entity) {
            // Now, actually calculate out our drop rates...
            for (MobPotionEffect matcher : this.getMatchers()) {
                PotionEffect effect = new PotionEffect(matcher.getWhat(), matcher.getDuration(), matcher.getAmplifier(),
                        false, matcher.isShowParticles());
                effect.setCurativeItems(Lists.newArrayList(matcher.getCures()));

                // Check if this drop matches:
                BaseMatchResult result = matcher.matches(entity, effect);
                if(!result.isMatch()) continue;

                // Make sure we have sane drop amounts:
                if (effect.getDuration() < 0) continue;

                // Now perform our updates:
                if(result.getCallback() != null) result.getCallback().run();

                // Do the drop:
                entity.addPotionEffect(effect);
            }
        }
    }

    // TODO: deserializer
}
