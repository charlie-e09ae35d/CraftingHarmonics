package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseHeldEquipmentMatcher extends BaseItemStackMatcher {
    private final ItemStack itemStack;

    public BaseHeldEquipmentMatcher(ItemStack itemStack, double damagePer, boolean consume, EnumHand hand) {
        super(damagePer, consume, hand);
        this.itemStack = itemStack;
    }

    protected BaseDropMatchResult matches(Entity entity, ItemStack drop) {
        if(entity == null || !EntityLivingBase.class.isAssignableFrom(entity.getClass())) return BaseDropMatchResult.False;

        // Get our entity and convert it over:
        EntityLivingBase entityBase = (EntityLivingBase) entity;

        ItemStack heldEquipment = entityBase.getHeldItem(hand);

        // Make sure we have held equipment and that it's right:
        if(heldEquipment == null || !heldEquipment.isItemEqualIgnoreDurability(itemStack)) return BaseDropMatchResult.True;

        return new BaseDropMatchResult(true, consumeOrDamageItem(entityBase, heldEquipment, drop));
    }
}
