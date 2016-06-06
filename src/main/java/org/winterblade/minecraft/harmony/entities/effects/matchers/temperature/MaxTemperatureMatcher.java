package org.winterblade.minecraft.harmony.entities.effects.matchers.temperature;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.temperature.matchers.BaseTemperatureMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"temperatureMax", "temperatureProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class MaxTemperatureMatcher extends BaseTemperatureMatcher implements IEntityMatcher {
    public MaxTemperatureMatcher(double temp) {
        this(temp, null);
    }

    public MaxTemperatureMatcher(double temp, @Nullable String provider) {
        super(Integer.MIN_VALUE, temp, provider);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, IEntityMatcherData iEntityMatcherData) {
        return matches(entity.getEntityWorld(), entity.getPosition());
    }
}
