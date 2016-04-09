package org.winterblade.minecraft.harmony.utility;

import net.minecraft.item.ItemStack;

/**
 * Created by Matt on 4/8/2016.
 */
public class OreDictionaryItemStack  {
    private final boolean isOreDict;
    private final String oreDictName;
    private final ItemStack itemStack;

    public OreDictionaryItemStack(String oreDictName) {
        this.oreDictName = oreDictName;
        this.isOreDict = true;
        this.itemStack = null;
    }

    public OreDictionaryItemStack(ItemStack itemStack) {
        this.isOreDict = false;
        this.itemStack = itemStack;
        this.oreDictName = null;
    }

    public String getOreDictName() {
        return oreDictName;
    }

    public boolean isOreDict() {
        return isOreDict;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
