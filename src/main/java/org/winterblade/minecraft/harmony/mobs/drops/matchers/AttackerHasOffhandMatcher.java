package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseHeldEquipmentMatcher;

/**
 * Created by Matt on 5/7/2016.
 */
@Component(properties = {"attackerHasOffhand", "consumeOffhand", "damageOffhandPer"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class AttackerHasOffhandMatcher extends BaseHeldEquipmentMatcher implements IMobDropMatcher {
    public AttackerHasOffhandMatcher(ItemStack offhand) {
        this(offhand, false);
    }

    public AttackerHasOffhandMatcher(ItemStack offhand, boolean consume) {
        this(offhand, consume, 0);
    }

    public AttackerHasOffhandMatcher(ItemStack offhand, boolean consume, double damagePer) {
        super(offhand, damagePer, consume, EnumHand.OFF_HAND);
    }


    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt  The event to match
     * @param drop The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        return matches(evt.getSource().getEntity(), drop);
    }

}
