package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseHasPotionMatcher;

/**
 * Created by Matt on 5/7/2016.
 */
@Component(properties = {"mobHasPotionEffect"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class MobHasPotionMatcher extends BaseHasPotionMatcher implements IMobDropMatcher {
    public MobHasPotionMatcher(Potion potion) {
        super(potion);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        return matches(evt.getEntity());
    }
}
