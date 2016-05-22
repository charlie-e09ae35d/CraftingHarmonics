package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseCooldownMatcher;

/**
 * Created by Matt on 5/22/2016.
 */
@Component(properties = "cooldown")
@PrioritizedObject(priority = Priority.LOWER)
public class CooldownMatcher extends BaseCooldownMatcher implements IMobDropMatcher {
    public CooldownMatcher(int cooldown) {
        super(cooldown);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        if(evt.getSource() == null || evt.getSource().getEntity() == null) return BaseMatchResult.False;
        return matches(evt.getSource().getEntity());
    }
}
