package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.ComponentParameter;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseHeldEquipmentMatcher;

/**
 * Created by Matt on 5/7/2016.
 */
@Component(properties = {"killedWith", "consume", "damagePer", "nbt", "fuzzyNbt"})
@PrioritizedObject(priority = Priority.HIGH)
public class KilledWithMatcher extends BaseHeldEquipmentMatcher implements IMobDropMatcher {
    // This is pretty much constructor hell...
    public KilledWithMatcher(ItemStack item) {
        this(item, false);
    }

    // Consume, up to including NBT
    public KilledWithMatcher(ItemStack item, boolean consume) {
        this(item, consume, 0.0, null, false);
    }

    public KilledWithMatcher(ItemStack item, boolean consume, @ComponentParameter(property = "nbt") NBTTagCompound nbt) {
        this(item, consume, 0.0, nbt, false);
    }

    public KilledWithMatcher(ItemStack item, boolean consume,
                                 @ComponentParameter(property = "nbt") NBTTagCompound nbt,
                                 @ComponentParameter(property = "fuzzyNbt") boolean fuzzyNbt) {
        this(item, consume, 0.0, nbt, fuzzyNbt);
    }

    // Damage per, up to including NBT
    public KilledWithMatcher(ItemStack item, @ComponentParameter(property = "damagePer") double damagePer) {
        this(item, false, damagePer, null, false);
    }

    public KilledWithMatcher(ItemStack item, @ComponentParameter(property = "damagePer") double damagePer,
                                 @ComponentParameter(property = "nbt") NBTTagCompound nbt) {
        this(item, false, damagePer, nbt, false);
    }

    public KilledWithMatcher(ItemStack item, @ComponentParameter(property = "damagePer") double damagePer,
                                 @ComponentParameter(property = "nbt") NBTTagCompound nbt,
                                 @ComponentParameter(property = "fuzzyNbt") boolean fuzzyNbt) {
        this(item, false, damagePer, nbt, fuzzyNbt);
    }

    // NBT only
    public KilledWithMatcher(ItemStack item,
                                 @ComponentParameter(property = "nbt") NBTTagCompound nbt) {
        this(item, nbt, false);
    }

    public KilledWithMatcher(ItemStack item,
                                 @ComponentParameter(property = "nbt") NBTTagCompound nbt,
                                 @ComponentParameter(property = "fuzzyNbt") boolean fuzzyNbt) {
        this(item, false, 0.0, nbt, fuzzyNbt);
    }

    // Full constructors
    public KilledWithMatcher(ItemStack item, boolean consume, double damagePer) {
        this(item, consume, damagePer, null);
    }

    public KilledWithMatcher(ItemStack item, boolean consume, double damagePer, NBTTagCompound nbt) {
        this(item, consume, damagePer, nbt, false);
    }

    public KilledWithMatcher(ItemStack item, boolean consume, double damagePer, NBTTagCompound nbt, boolean fuzzyNbt) {
        super(item, damagePer, consume, nbt, fuzzyNbt, EnumHand.MAIN_HAND);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        return matches(evt.getSource().getEntity(), drop);
    }
}
