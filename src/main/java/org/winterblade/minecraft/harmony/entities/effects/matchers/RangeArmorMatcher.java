package org.winterblade.minecraft.harmony.entities.effects.matchers;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.dto.RangePair;
import org.winterblade.minecraft.harmony.common.matchers.BaseArmorValueMatcher;

/**
 * Created by Matt on 6/29/2016.
 */
@Component(properties = {"armorRange"})
public class RangeArmorMatcher extends BaseArmorValueMatcher implements IEntityMatcher {
    public RangeArmorMatcher(RangePair data) {
        super(data.getMin(), data.getMax());
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata metadata) {
        return matches(entity);
    }
}