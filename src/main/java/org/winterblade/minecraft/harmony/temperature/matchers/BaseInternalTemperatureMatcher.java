package org.winterblade.minecraft.harmony.temperature.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.temperature.TemperatureRegistry;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
public abstract class BaseInternalTemperatureMatcher {
    private final double minTemp;
    private final double maxTemp;
    @Nullable
    private final String provider;

    public BaseInternalTemperatureMatcher(double minTemp, double maxTemp, @Nullable String provider) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.provider = provider;
    }

    protected BaseMatchResult matches(Entity entity) {
        if(!EntityLivingBase.class.isAssignableFrom(entity.getClass())) return BaseMatchResult.False;
        double temp = TemperatureRegistry.instance.getInternalTemperature((EntityLivingBase) entity, provider);
        return minTemp <= temp && temp <= maxTemp ? BaseMatchResult.True : BaseMatchResult.False;
    }
}
