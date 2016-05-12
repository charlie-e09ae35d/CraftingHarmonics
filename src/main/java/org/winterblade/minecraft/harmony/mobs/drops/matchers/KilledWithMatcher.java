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
@Component(properties = {"killedWith", "consume", "damagePer"})
@PrioritizedObject(priority = Priority.HIGH)
public class KilledWithMatcher extends BaseHeldEquipmentMatcher implements IMobDropMatcher {
    public KilledWithMatcher(ItemStack killedWith) {
        this(killedWith, false);
    }

    public KilledWithMatcher(ItemStack killedWith, boolean consume) {
        this(killedWith, consume, 0);
    }

    public KilledWithMatcher(ItemStack killedWith, boolean consume, double damagePer) {
        super(killedWith, damagePer, consume, EnumHand.MAIN_HAND);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        return matches(evt.getSource().getEntity(), drop);
    }
}
