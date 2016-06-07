package org.winterblade.minecraft.harmony.entities.effects.matchers.temperature;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.temperature.matchers.BaseInternalTemperatureMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"temperatureInternalMin", "temperatureProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class MinInternalTemperatureMatcher extends BaseInternalTemperatureMatcher implements IEntityMatcher {
    public MinInternalTemperatureMatcher(double temp) {
        this(temp, null);
    }

    public MinInternalTemperatureMatcher(double temp, @Nullable String provider) {
        super(temp, Integer.MAX_VALUE, provider);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, IEntityMatcherData iEntityMatcherData) {
        return matches(entity);
    }
}