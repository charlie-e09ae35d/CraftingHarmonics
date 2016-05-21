package org.winterblade.minecraft.harmony.api.mobs.sheds;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.drops.IBaseDropMatcher;

/**
 * A matcher for a mob shed event; implementations should also be annotated with @Component and @PrioritizedObject
 */
public interface IMobShedMatcher extends IBaseDropMatcher<EntityLivingBase> {}
