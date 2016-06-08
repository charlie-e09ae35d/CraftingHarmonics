package org.winterblade.minecraft.harmony.entities.effects.matchers.temperature;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.temperature.matchers.BaseTemperatureMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"temperatureMin", "temperatureProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class MinTemperatureMatcher extends BaseTemperatureMatcher implements IEntityMatcher {
    public MinTemperatureMatcher(double temp) {
        this(temp, null);
    }

    public MinTemperatureMatcher(double temp, @Nullable String provider) {
        super(temp, Integer.MAX_VALUE, provider);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata iEntityMatcherData) {
        return matches(entity.getEntityWorld(), entity.getPosition());
    }
}
