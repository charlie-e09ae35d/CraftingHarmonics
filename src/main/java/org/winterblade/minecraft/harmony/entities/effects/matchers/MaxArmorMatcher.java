package org.winterblade.minecraft.harmony.entities.effects.matchers;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.matchers.BaseArmorValueMatcher;

/**
 * Created by Matt on 6/29/2016.
 */
@Component(properties = {"armorMax"})
public class MaxArmorMatcher extends BaseArmorValueMatcher implements IEntityMatcher {
    public MaxArmorMatcher(int max) {
        super(Integer.MIN_VALUE, max);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata metadata) {
        return matches(entity);
    }
}