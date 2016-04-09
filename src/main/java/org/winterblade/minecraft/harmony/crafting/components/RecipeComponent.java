package org.winterblade.minecraft.harmony.crafting.components;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Matt on 4/8/2016.
 */
public class RecipeComponent {
    protected ItemStack item;
    protected NBTTagCompound nbt;
    private String oreDict;
    private boolean fuzzyNbt;
    private boolean returnOnCraft;
    private RecipeComponent replace;

    public ItemStack getItemStack() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public NBTTagCompound getNbt() {
        return nbt;
    }

    public void setNbt(NBTTagCompound nbt) {
        this.nbt = nbt;
    }

    public String getOreDictName() {
        return oreDict;
    }

    public void setOreDict(String oreDict) {
        this.oreDict = oreDict;
    }

    public boolean isOreDict() {
        return oreDict != null;
    }

    public boolean hasNbt() {
        return nbt != null && !nbt.hasNoTags();
    }
}

