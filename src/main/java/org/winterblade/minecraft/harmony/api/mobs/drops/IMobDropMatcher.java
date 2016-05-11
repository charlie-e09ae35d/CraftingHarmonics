package org.winterblade.minecraft.harmony.api.mobs.drops;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.mobs.IBaseMobDropMatcher;

/**
 * A matcher for a mob drop event; implementations should also be annotated with @Component and @PrioritizedObject
 */
public interface IMobDropMatcher extends IBaseMobDropMatcher<LivingDropsEvent> {}
