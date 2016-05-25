package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseChanceMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"chance"})
@PrioritizedObject(priority = Priority.HIGHEST)
public class ChanceMatcher extends BaseChanceMatcher implements IEntityMatcher {
    public ChanceMatcher(float chance) {
        super(chance);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase evt, IEntityMatcherData metadata) {
        return match(evt.getEntityWorld().rand);
    }
}
