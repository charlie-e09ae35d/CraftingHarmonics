package org.winterblade.minecraft.harmony.crafting.components;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Matt on 4/8/2016.
 */
public class BaseRecipeComponent {
    protected ItemStack item;
    protected NBTTagCompound nbt;

    public ItemStack getItemStack() {
        return item;
    }

    public NBTTagCompound getNbt() {
        return nbt;
    }
}

