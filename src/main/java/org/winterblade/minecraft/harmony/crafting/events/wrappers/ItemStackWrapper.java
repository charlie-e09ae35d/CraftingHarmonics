package org.winterblade.minecraft.harmony.crafting.events.wrappers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;

public class ItemStackWrapper {
    private ItemStack itemStack;

    public ItemStackWrapper(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Sets the item's stack size to zero, destroying the item.
     */
    public ItemStackWrapper destroyItem() {
        itemStack.stackSize = 0;
        return this;
    }

    /**
     * Sets the item's stack size to one, returning the item.
     */
    public ItemStackWrapper returnItem() {
        itemStack.stackSize = 1;
        return this;
    }

    public ItemStackWrapper replaceItem(String id) throws OperationException {
        itemStack = ItemUtility.translateToItemStack(id);
        return this;
    }

    /**
     * Damages the item by 1
     */
    public ItemStackWrapper damageItem() {
        return damageItem(1);
    }

    /**
     * Damages the item by a given amount
     * @param by    The amount to damage it by
     */
    public ItemStackWrapper damageItem(int by) {
        itemStack.setItemDamage(itemStack.getItemDamage()+by);
        return this;
    }

    /**
     * Gets the item's metadata
     * @return  The metadata
     */
    public int getMetadata() {
        return itemStack.getMetadata();
    }

    /**
     * Gets the item's current damage
     * @return  The item's current damage value
     */
    public int getDamage() {
        return itemStack.getItemDamage();
    }

    /**
     * Gets the item's resource locator name
     * @return  The item's resource locator
     */
    public String getId() {
        return Item.REGISTRY.getNameForObject(itemStack.getItem()).toString();
    }

    /**
     * Returns the item stack itself
     * @return  The item stack we're wrapping.
     */
    public ItemStack getItemStack() {
        return itemStack;
    }
}
