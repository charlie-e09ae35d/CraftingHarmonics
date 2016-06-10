package org.winterblade.minecraft.harmony.mobs.sheds.matchers.temperature;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.temperature.matchers.BaseTemperatureMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"temperatureMax", "temperatureProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class MaxTemperatureMatcher extends BaseTemperatureMatcher implements IMobShedMatcher {
    public MaxTemperatureMatcher(double temp) {
        this(temp, null);
    }

    public MaxTemperatureMatcher(double temp, @Nullable String provider) {
        super(Integer.MIN_VALUE, temp, provider);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entityLivingBase The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entityLivingBase, ItemStack drop) {
        return matches(entityLivingBase.getEntityWorld(), entityLivingBase.getPosition());
    }
}
