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
@Component(properties = {"armorMin"})
public class MinArmorMatcher extends BaseArmorValueMatcher implements IEntityMatcher {
    public MinArmorMatcher(int min) {
        super(min, Integer.MAX_VALUE);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata metadata) {
        return matches(entity);
    }
}