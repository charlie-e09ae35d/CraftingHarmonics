package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseLightLevelMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"minLightLevel"})
@PrioritizedObject(priority = Priority.HIGH)
public class MinLightLevelMatcher extends BaseLightLevelMatcher implements IEntityMatcher {
    public MinLightLevelMatcher(int min) {
        super(min,15);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity           The event to match
     * @param metadata         Event metadata.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entity, IEntityMatcherData metadata) {
        return matches(entity);
    }
}