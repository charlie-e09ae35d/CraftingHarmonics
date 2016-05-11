package org.winterblade.minecraft.harmony.api.mobs.sheds;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.winterblade.minecraft.harmony.api.mobs.IBaseMobDropMatcher;

/**
 * A matcher for a mob shed event; implementations should also be annotated with @Component and @PrioritizedObject
 */
public interface IMobShedMatcher extends IBaseMobDropMatcher<EntityLiving> {}
