package org.winterblade.minecraft.harmony.entities.effects.matchers;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseIsRainingMatcher;

/**
 * Created by Matt on 5/23/2016.
 */
@Component(properties = {"isRaining"})
@PrioritizedObject(priority = Priority.HIGHER)
public class IsRainingMatcher extends BaseIsRainingMatcher implements IEntityMatcher {
    public IsRainingMatcher(WeatherMatcher matcher) {
        super(matcher);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param event             The event to match
     * @param metadata          Event metadata.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(Entity event, IEntityMatcherData metadata) {
        return matches(event.getEntityWorld(), event.getPosition());
    }
}