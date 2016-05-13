package org.winterblade.minecraft.harmony.api.mobs.drops;

import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.drops.IBaseDropMatcher;

/**
 * A matcher for a mob drop event; implementations should also be annotated with @Component and @PrioritizedObject
 */
public interface IMobDropMatcher extends IBaseDropMatcher<LivingDropsEvent> {}
