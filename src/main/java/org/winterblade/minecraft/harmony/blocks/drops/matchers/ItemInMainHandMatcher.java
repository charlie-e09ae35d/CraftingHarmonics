package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.ComponentParameter;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.common.matchers.BaseHeldEquipmentMatcher;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"minedWith", "consume", "damagePer", "nbt", "fuzzyNbt"})
@PrioritizedObject(priority = Priority.HIGH)
public class ItemInMainHandMatcher extends BaseHeldEquipmentMatcher implements IBlockDropMatcher {
    // This is pretty much constructor hell...
    public ItemInMainHandMatcher(ItemStack item) {
        this(item, false);
    }

    // Consume, up to including NBT
    public ItemInMainHandMatcher(ItemStack item, boolean consume) {
        this(item, consume, 0.0, null, false);
    }

    public ItemInMainHandMatcher(ItemStack item, boolean consume, @ComponentParameter(property = "nbt") NBTTagCompound nbt) {
        this(item, consume, 0.0, nbt, false);
    }

    public ItemInMainHandMatcher(ItemStack item, boolean consume,
                                 @ComponentParameter(property = "nbt") NBTTagCompound nbt,
                                 @ComponentParameter(property = "fuzzyNbt") boolean fuzzyNbt) {
        this(item, consume, 0.0, nbt, fuzzyNbt);
    }

    // Damage per, up to including NBT
    public ItemInMainHandMatcher(ItemStack item, @ComponentParameter(property = "damagePer") double damagePer) {
        this(item, false, damagePer, null, false);
    }

    public ItemInMainHandMatcher(ItemStack item, @ComponentParameter(property = "damagePer") double damagePer,
                                 @ComponentParameter(property = "nbt") NBTTagCompound nbt) {
        this(item, false, damagePer, nbt, false);
    }

    public ItemInMainHandMatcher(ItemStack item, @ComponentParameter(property = "damagePer") double damagePer,
                                 @ComponentParameter(property = "nbt") NBTTagCompound nbt,
                                 @ComponentParameter(property = "fuzzyNbt") boolean fuzzyNbt) {
        this(item, false, damagePer, nbt, fuzzyNbt);
    }

    // NBT only
    public ItemInMainHandMatcher(ItemStack item,
                                 @ComponentParameter(property = "nbt") NBTTagCompound nbt) {
        this(item, nbt, false);
    }

    public ItemInMainHandMatcher(ItemStack item,
                                 @ComponentParameter(property = "nbt") NBTTagCompound nbt,
                                 @ComponentParameter(property = "fuzzyNbt") boolean fuzzyNbt) {
        this(item, false, 0.0, nbt, fuzzyNbt);
    }

    // Full constructors
    public ItemInMainHandMatcher(ItemStack item, boolean consume, double damagePer) {
        this(item, consume, damagePer, null);
    }

    public ItemInMainHandMatcher(ItemStack item, boolean consume, double damagePer, NBTTagCompound nbt) {
        this(item, consume, damagePer, nbt, false);
    }

    public ItemInMainHandMatcher(ItemStack item, boolean consume, double damagePer, NBTTagCompound nbt, boolean fuzzyNbt) {
        super(item, damagePer, consume, nbt, fuzzyNbt, EnumHand.MAIN_HAND);
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
