package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.common.ItemUtility;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseHeldEquipmentMatcher extends BaseItemStackMatcher {
    private final ItemStack itemStack;
    @Nullable
    private final NBTTagCompound nbt;
    private final boolean fuzzyNbt;

    public BaseHeldEquipmentMatcher(ItemStack itemStack, double damagePer, boolean consume, @Nullable NBTTagCompound nbt,
                                    boolean fuzzyNbt, EnumHand hand) {
        super(damagePer, consume, hand);
        this.itemStack = itemStack;
        this.nbt = nbt;
        this.fuzzyNbt = fuzzyNbt;
    }

    protected BaseMatchResult matches(Entity entity, ItemStack drop) {
        if(entity == null || !EntityLivingBase.class.isAssignableFrom(entity.getClass())) return BaseMatchResult.False;

        // Get our entity and convert it over:
        EntityLivingBase entityBase = (EntityLivingBase) entity;

        ItemStack heldEquipment = entityBase.getHeldItem(hand);

        // Make sure we have held equipment and that it's right:
        if(heldEquipment == null
                || !heldEquipment.isItemEqualIgnoreDurability(itemStack)
                || (nbt != null && !ItemUtility.checkIfNbtMatches(nbt, heldEquipment.getTagCompound(), fuzzyNbt))) return BaseMatchResult.False;

        return new BaseMatchResult(true, consumeOrDamageItem(entityBase, heldEquipment, drop));
    }
}
