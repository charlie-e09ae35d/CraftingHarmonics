package org.winterblade.minecraft.harmony.mobs.effects;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.mobs.effects.IMobPotionEffectMatcher;

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
        return cures;
    }
}
