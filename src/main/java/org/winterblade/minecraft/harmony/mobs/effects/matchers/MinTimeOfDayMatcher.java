package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseTimeOfDayMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = "minTimeOfDay")
@PrioritizedObject(priority = Priority.HIGH)
public class MinTimeOfDayMatcher extends BaseTimeOfDayMatcher implements IEntityMatcher {
    public MinTimeOfDayMatcher(long minTime) {super(minTime, Long.MAX_VALUE);}

    @Override
    public BaseMatchResult isMatch(Entity entity, IEntityMatcherData metadata) {
        return matches(entity.getEntityWorld());
    }
}
