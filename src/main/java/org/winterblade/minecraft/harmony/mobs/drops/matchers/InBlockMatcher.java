package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.common.blocks.BlockMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseBlockMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/15/2016.
 */
@Component(properties = {"insideBlock"})
@PrioritizedObject(priority = Priority.HIGH)
public class InBlockMatcher extends BaseBlockMatcher implements IMobDropMatcher {
    public InBlockMatcher(@Nullable BlockMatcher matcher) {
        super(matcher);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param livingDropsEvent The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(LivingDropsEvent livingDropsEvent, ItemStack drop) {
        return matches(livingDropsEvent.getEntity().getEntityWorld(), livingDropsEvent.getEntity().getPosition());
    }
}
