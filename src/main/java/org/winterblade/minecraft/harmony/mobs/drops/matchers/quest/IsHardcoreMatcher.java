package org.winterblade.minecraft.harmony.mobs.drops.matchers.quest;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.quests.matchers.BaseIsHardcoreMatcher;

/**
 * Created by Matt on 5/30/2016.
 */
@Component(properties = {"questingIsHardcore"})
@PrioritizedObject(priority = Priority.HIGH)
public class IsHardcoreMatcher extends BaseIsHardcoreMatcher implements IMobDropMatcher {
    public IsHardcoreMatcher(boolean isHardcore) {
        super(isHardcore);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param event The event to match
     * @param drop  The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(LivingDropsEvent event, ItemStack drop) {
        return matches();
    }
}