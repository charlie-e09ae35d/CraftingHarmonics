package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.mobs.drops.MobDrop;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"not"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class NotMatcher implements IMobDropMatcher {
    private final MobDrop composite;

    public NotMatcher(MobDrop composite) {
        this.composite = composite;
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param livingDropsEvent The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(LivingDropsEvent livingDropsEvent, ItemStack drop) {
        // We aren't going to run any of the callbacks from here, as they're inverted.
        BaseDropMatchResult result = composite.matches(livingDropsEvent, drop);

        // Just invert the match:
        return new BaseDropMatchResult(!result.isMatch());
    }
}
