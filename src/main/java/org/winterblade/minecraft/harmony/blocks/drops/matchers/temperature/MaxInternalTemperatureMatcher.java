package org.winterblade.minecraft.harmony.blocks.drops.matchers.temperature;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.temperature.matchers.BaseInternalTemperatureMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/6/2016.
 */
@Component(properties = {"temperatureInternalMax", "temperatureProvider" })
@PrioritizedObject(priority = Priority.MEDIUM)
public class MaxInternalTemperatureMatcher extends BaseInternalTemperatureMatcher implements IBlockDropMatcher {
    public MaxInternalTemperatureMatcher(double temp) {
        this(temp, null);
    }

    public MaxInternalTemperatureMatcher(double temp, @Nullable String provider) {
        super(Integer.MIN_VALUE, temp, provider);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param harvestDropsEvent The event to match
     * @param drop              The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(BlockEvent.HarvestDropsEvent harvestDropsEvent, ItemStack drop) {
        if(harvestDropsEvent.getHarvester() == null) return BaseMatchResult.False;
        return matches(harvestDropsEvent.getHarvester());
    }
}