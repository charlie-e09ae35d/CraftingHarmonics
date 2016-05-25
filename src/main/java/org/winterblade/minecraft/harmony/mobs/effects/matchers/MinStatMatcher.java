package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.dto.NameValuePair;
import org.winterblade.minecraft.harmony.common.matchers.BaseStatMatcher;

/**
 * Created by Matt on 5/23/2016.
 */
@Component(properties = {"minStat"})
@PrioritizedObject(priority = Priority.HIGH)
public class MinStatMatcher extends BaseStatMatcher implements IEntityMatcher {
    public MinStatMatcher(NameValuePair<Integer> stat) {
        super(stat.getName(), stat.getValue(), Integer.MAX_VALUE);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, IEntityMatcherData metadata) {
        return matches(entity);
    }
}