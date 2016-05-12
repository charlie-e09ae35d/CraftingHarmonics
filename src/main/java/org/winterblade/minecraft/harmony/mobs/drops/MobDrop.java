package org.winterblade.minecraft.harmony.mobs.drops;

import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.drops.BaseDrop;

/**
 * Created by Matt on 5/4/2016.
 */
public class MobDrop extends BaseDrop<LivingDropsEvent, IMobDropMatcher> {
    private double lootingMultiplier;

    public double getLootingMultiplier() {
        return lootingMultiplier;
    }

    public void setLootingMultiplier(Double lootingMultiplier) {
        this.lootingMultiplier = lootingMultiplier;
    }

}
