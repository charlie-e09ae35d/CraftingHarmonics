package org.winterblade.minecraft.harmony.crafting.components;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.utility.OreDictionaryItemStack;

/**
 * Created by Matt on 4/8/2016.
 */
public class RecipeComponent {
    protected OreDictionaryItemStack item;
    protected NBTTagCompound nbt;
    private boolean fuzzyNbt;
    private boolean returnOnCraft;
    private RecipeComponent replace;

    public ItemStack getItemStack() {
        return item.getItemStack();
    }

    public void setItem(ItemStack item) {
        this.item = new OreDictionaryItemStack(item);
    }

    public NBTTagCompound getNbt() {
        return nbt;
    }

    public void setNbt(NBTTagCompound nbt) {
        this.nbt = nbt;
    }

    public String getOreDictName() {
        return item.getOreDictName();
    }

    public void setOreDict(String oreDict) {
        this.item = new OreDictionaryItemStack(oreDict);
    }

    public boolean isOreDict() {
        return item.isOreDict();
    }

    public boolean hasNbt() {
        return nbt != null && !nbt.hasNoTags();
    }
}

