package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.ComponentParameter;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseInventoryMatcher;

/**
 * Created by Matt on 5/7/2016.
 */
@Component(properties = {"playerHasInventory", "consumeInventory", "damageInventoryPer", "nbtInventory", "fuzzyNbtInventory"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class PlayerHasInventoryMatcher extends BaseInventoryMatcher implements IMobDropMatcher {
    public PlayerHasInventoryMatcher(ItemStack item) {
        this(item, false);
    }

    // Consume, up to including NBT
    public PlayerHasInventoryMatcher(ItemStack item, boolean consume) {
        this(item, consume, 0.0, null, false);
    }

    public PlayerHasInventoryMatcher(ItemStack item, boolean consume, @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt) {
        this(item, consume, 0.0, nbt, false);
    }

    public PlayerHasInventoryMatcher(ItemStack item, boolean consume,
                                  @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt,
                                  @ComponentParameter(property = "fuzzyNbtInventory") boolean fuzzyNbt) {
        this(item, consume, 0.0, nbt, fuzzyNbt);
    }

    // Damage per, up to including NBT
    public PlayerHasInventoryMatcher(ItemStack item, @ComponentParameter(property = "damageInventoryPer") double damagePer) {
        this(item, false, damagePer, null, false);
    }

    public PlayerHasInventoryMatcher(ItemStack item, @ComponentParameter(property = "damageInventoryPer") double damagePer,
                                  @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt) {
        this(item, false, damagePer, nbt, false);
    }

    public PlayerHasInventoryMatcher(ItemStack item, @ComponentParameter(property = "damageInventoryPer") double damagePer,
                                  @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt,
                                  @ComponentParameter(property = "fuzzyNbtInventory") boolean fuzzyNbt) {
        this(item, false, damagePer, nbt, fuzzyNbt);
    }

    // NBT only
    public PlayerHasInventoryMatcher(ItemStack item,
                                  @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt) {
        this(item, nbt, false);
    }

    public PlayerHasInventoryMatcher(ItemStack item,
                                  @ComponentParameter(property = "nbtInventory") NBTTagCompound nbt,
                                  @ComponentParameter(property = "fuzzyNbtInventory") boolean fuzzyNbt) {
        this(item, false, 0.0, nbt, fuzzyNbt);
    }

    // Full constructors
    public PlayerHasInventoryMatcher(ItemStack item, boolean consume, double damagePer) {
        this(item, consume, damagePer, null);
    }

    public PlayerHasInventoryMatcher(ItemStack item, boolean consume, double damagePer, NBTTagCompound nbt) {
        this(item, consume, damagePer, nbt, false);
    }

    public PlayerHasInventoryMatcher(ItemStack item, boolean consume, double damagePer, NBTTagCompound nbt, boolean fuzzyNbt) {
        super(item, damagePer, consume, nbt, fuzzyNbt, null);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt  The event to match
     * @param drop The dropped requiredItem; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        return matches(evt.getSource().getEntity(), drop);
    }
}
