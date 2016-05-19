package org.winterblade.minecraft.harmony.api.crafting.components;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.utility.OreDictionaryItemStack;

/**
 * Created by Matt on 4/8/2016.
 */
public class RecipeComponent {
    protected OreDictionaryItemStack item;
    private boolean fuzzyNbt;

    private boolean returnOnCraft;
    private Item replaceOnCraft;

    /**
     * Convenience method to translate an array of RecipeComponents into an array of ItemStacks
     * @param components    The RecipeComponents to translate
     * @return              The ItemStack[]
     */
    public static ItemStack[] getItemStacks(RecipeComponent[] components) {
        ItemStack[] items = new ItemStack[components.length];
        for (int i = 0; i < components.length; i++) {
            items[i] = components[i].getItemStack();
        }
        return items;
    }

    /*
     *  Getters / Setters
     */
    public ItemStack getItemStack() {
        return item == null ? null : item.getItemStack();
    }

    public void setItemStack(String resourceLocator, ItemStack item) {
        this.item = new OreDictionaryItemStack(resourceLocator, item);
    }

    public NBTTagCompound getNbt() {
        return item == null || item.getItemStack() == null ? null : item.getItemStack().getTagCompound();
    }

    public void setNbt(NBTTagCompound nbt) {
        if(this.item != null && item.getItemStack() != null) item.getItemStack().setTagCompound(nbt);
    }

    public String getOreDictName() {
        return item == null ? null : item.getOreDictName();
    }

    public void setOreDictName(String resourceLocator, String oreDict) {
        this.item = new OreDictionaryItemStack(resourceLocator, oreDict);
    }

    public boolean isOreDict() {
        return item != null && item.isOreDict();
    }

    public boolean hasNbt() {
        return !(item == null || item.getItemStack() == null) && item.getItemStack().hasTagCompound();
    }

    public boolean isFuzzyNbt() {
        return fuzzyNbt;
    }

    @Override
    public String toString() {
        return item == null ? "null" : item.toString();
    }

    public void setReturnOnCraft(boolean returnOnCraft) {
        this.returnOnCraft = returnOnCraft;

        if(returnOnCraft && !item.isOreDict()) {
            item.getItemStack().getItem().setContainerItem(item.getItemStack().getItem());
        }
    }

    public boolean isReturnOnCraft() {
        return returnOnCraft;
    }

    public void setReplaceOnCraft(Item replacingItem) {
        this.replaceOnCraft = replacingItem;

        if(replaceOnCraft != null && !item.isOreDict()) {
            item.getItemStack().getItem().setContainerItem(replacingItem);
        }
    }
}

