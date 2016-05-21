package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseChanceMatcher;

/**
 * Created by Matt on 5/7/2016.
 */
@Component(properties = {"chance"})
@PrioritizedObject(priority = Priority.HIGHEST)
public class ChanceMatcher extends BaseChanceMatcher implements IMobShedMatcher {
    public ChanceMatcher(float chance) {
        super(chance);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLiving evt, ItemStack drop) {
        return match(evt.getEntityWorld().rand);
    }
}
