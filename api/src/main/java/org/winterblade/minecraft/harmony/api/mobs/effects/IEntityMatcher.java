package org.winterblade.minecraft.harmony.api.mobs.effects;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.IMatcher;

/**
 * A matcher for a mob potion effect event; implementations should also be annotated with @Component and @PrioritizedObject
 */
public interface IEntityMatcher extends IMatcher<Entity,IEntityMatcherData> {}
