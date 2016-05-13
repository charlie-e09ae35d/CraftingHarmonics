package org.winterblade.minecraft.harmony.api.blocks;

import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.drops.IBaseDropMatcher;

/**
 * A matcher for a block drop event; implementations should also be annotated with @Component and @PrioritizedObject
 */
public interface IBlockDropMatcher extends IBaseDropMatcher<BlockEvent.HarvestDropsEvent> {}
