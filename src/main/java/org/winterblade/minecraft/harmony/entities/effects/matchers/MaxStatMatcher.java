package org.winterblade.minecraft.harmony.entities.effects.matchers;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.dto.NameValuePair;
import org.winterblade.minecraft.harmony.common.matchers.BaseStatMatcher;

/**
 * Created by Matt on 5/23/2016.
 */
@Component(properties = {"maxStat"})
@PrioritizedObject(priority = Priority.HIGH)
public class MaxStatMatcher extends BaseStatMatcher implements IEntityMatcher {
    public MaxStatMatcher(NameValuePair<Integer> stat) {
        super(stat.getName(), Integer.MIN_VALUE, stat.getValue());
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata metadata) {
        return matches(entity);
    }
}