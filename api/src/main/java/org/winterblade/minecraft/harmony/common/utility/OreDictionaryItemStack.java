package org.winterblade.minecraft.harmony.common.utility;

import net.minecraft.item.ItemStack;

/**
 * Created by Matt on 4/8/2016.
 */
public class OreDictionaryItemStack  {
    private final String resourceLocator;
    private final boolean isOreDict;
    private final String oreDictName;
    private final ItemStack itemStack;

    public OreDictionaryItemStack(String resourceLocator, String oreDictName) {
        this.resourceLocator = resourceLocator;
        this.oreDictName = oreDictName;
        this.isOreDict = true;
        this.itemStack = null;
    }

    public OreDictionaryItemStack(String resourceLocator, ItemStack itemStack) {
        this.resourceLocator = resourceLocator;
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

    @Override
    public String toString() {
        // TODO: Pass the actual name in here so we can get better logging later.
        return resourceLocator;
    }
}
