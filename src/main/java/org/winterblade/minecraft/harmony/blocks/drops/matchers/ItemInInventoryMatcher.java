package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.ComponentParameter;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.drops.matchers.BaseInventoryMatcher;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"itemInInventory", "consumeInventory", "damageInventoryPer", "nbtInventory", "fuzzyNbtInventory"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class ItemInInventoryMatcher extends BaseInventoryMatcher implements IBlockDropMatcher {
    public ItemInInventoryMatcher(ItemStack item) {
        this(item, false);
    }

    // Consume, up to including NBT
    public ItemInInventoryMatcher(ItemStack item, boolean consume) {
        this(item, consume, 0.0, null, false);
    }

    public ItemInInventoryMatcher(ItemStack item, boolean consume, @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt) {
        this(item, consume, 0.0, nbt, false);
    }

    public ItemInInventoryMatcher(ItemStack item, boolean consume,
                                     @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt,
                                     @ComponentParameter(property = "fuzzyNbtInventory") boolean fuzzyNbt) {
        this(item, consume, 0.0, nbt, fuzzyNbt);
    }

    // Damage per, up to including NBT
    public ItemInInventoryMatcher(ItemStack item, @ComponentParameter(property = "damageInventoryPer") double damagePer) {
        this(item, false, damagePer, null, false);
    }

    public ItemInInventoryMatcher(ItemStack item, @ComponentParameter(property = "damageInventoryPer") double damagePer,
                                     @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt) {
        this(item, false, damagePer, nbt, false);
    }

    public ItemInInventoryMatcher(ItemStack item, @ComponentParameter(property = "damageInventoryPer") double damagePer,
                                     @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt,
                                     @ComponentParameter(property = "fuzzyNbtInventory") boolean fuzzyNbt) {
        this(item, false, damagePer, nbt, fuzzyNbt);
    }

    // NBT only
    public ItemInInventoryMatcher(ItemStack item,
                                     @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt) {
        this(item, nbt, false);
    }

    public ItemInInventoryMatcher(ItemStack item,
                                     @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt,
                                     @ComponentParameter(property = "fuzzyNbtInventory") boolean fuzzyNbt) {
        this(item, false, 0.0, nbt, fuzzyNbt);
    }

    // Full constructors
    public ItemInInventoryMatcher(ItemStack item, boolean consume, double damagePer) {
        this(item, consume, damagePer, null);
    }

    public ItemInInventoryMatcher(ItemStack item, boolean consume, double damagePer, NBTTagCompound nbt) {
        this(item, consume, damagePer, nbt, false);
    }

    public ItemInInventoryMatcher(ItemStack item, boolean consume, double damagePer, NBTTagCompound nbt, boolean fuzzyNbt) {
        super(item, damagePer, consume, nbt, fuzzyNbt, null);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param harvestDropsEvent The event to match
     * @param drop              The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(BlockEvent.HarvestDropsEvent harvestDropsEvent, ItemStack drop) {
        return matches(harvestDropsEvent.getHarvester(), drop);
    }
}
