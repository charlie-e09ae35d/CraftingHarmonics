package org.winterblade.minecraft.harmony.entities.effects.matchers;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseMoonPhaseMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"moonPhase"})
@PrioritizedObject(priority = Priority.HIGH)
public class MoonPhaseMatcher extends BaseMoonPhaseMatcher implements IEntityMatcher {
    public MoonPhaseMatcher(String phase) {
        super(MoonPhase.valueOf(phase));
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity The event to match
     * @param metadata Event metadata.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(Entity entity, IEntityMatcherData metadata) {
        return matches(entity.getEntityWorld());
    }
}
