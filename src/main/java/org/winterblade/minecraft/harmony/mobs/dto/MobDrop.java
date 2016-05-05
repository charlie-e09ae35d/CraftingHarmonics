package org.winterblade.minecraft.harmony.mobs.dto;

import net.minecraft.item.ItemStack;

/**
 * Created by Matt on 5/4/2016.
 */
public class MobDrop {
    private ItemStack what;
    private int min;
    private int max;
    private float chance;
    private float lootingMultiplier;

    public ItemStack getWhat() {
        return what;
    }

    public int getMin() {
        return min < 0 ? 0 : min;
    }

    public int getMax() {
        return max < 0 ? 0 : max;
    }

    public float getChance() {
        return chance < 0 ? 0.0f : chance;
    }
}
