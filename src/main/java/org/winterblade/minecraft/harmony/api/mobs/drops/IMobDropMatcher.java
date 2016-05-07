package org.winterblade.minecraft.harmony.api.mobs.drops;

import net.minecraftforge.event.entity.living.LivingDropsEvent;

/**
 * A matcher for a mob drop event; implementations should also be annotated with @Component and @PrioritizedObject
 */
public interface IMobDropMatcher {
    /**
     * Should return true if this matcher matches the given event
     * @param evt    The event to match
     * @return       True if it should match; false otherwise
     */
    boolean isMatch(LivingDropsEvent evt);
}
