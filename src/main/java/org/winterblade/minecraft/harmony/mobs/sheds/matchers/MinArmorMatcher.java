package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseArmorValueMatcher;

/**
 * Created by Matt on 6/29/2016.
 */
@Component(properties = {"armorMin"})
public class MinArmorMatcher extends BaseArmorValueMatcher implements IMobShedMatcher {
    public MinArmorMatcher(int min) {
        super(min, Integer.MAX_VALUE);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entityLivingBase The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entityLivingBase, ItemStack drop) {
        return matches(entityLivingBase);
    }
}