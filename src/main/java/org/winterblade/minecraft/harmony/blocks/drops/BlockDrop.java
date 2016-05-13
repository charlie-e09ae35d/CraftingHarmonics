package org.winterblade.minecraft.harmony.blocks.drops;

import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.drops.BaseDrop;

/**
 * Created by Matt on 5/12/2016.
 */
public class BlockDrop extends BaseDrop<BlockEvent.HarvestDropsEvent, IBlockDropMatcher> {
    private double fortuneMultiplier;

    public double getFortuneMultiplier() {
        return fortuneMultiplier;
    }

    public void setFortuneMultiplier(Double fortuneMultiplier) {
        this.fortuneMultiplier = fortuneMultiplier;
    }

}
