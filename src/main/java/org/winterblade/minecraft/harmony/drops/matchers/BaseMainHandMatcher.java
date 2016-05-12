package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseMainHandMatcher extends BaseItemStackMatcher {
    private final ItemStack itemStack;

    public BaseMainHandMatcher(ItemStack itemStack, double damagePer, boolean consume, EnumHand hand) {
        super(damagePer, consume, hand);
        this.itemStack = itemStack;
    }

    protected boolean matches(Entity entity, ItemStack drop) {
        if(entity == null || !EntityLivingBase.class.isAssignableFrom(entity.getClass())) return false;

        // Get our entity and convert it over:
        EntityLivingBase entityBase = (EntityLivingBase) entity;

        ItemStack heldEquipment = entityBase.getHeldItemMainhand();

        // Make sure we have held equipment and that it's right:
        if(heldEquipment == null || !heldEquipment.isItemEqualIgnoreDurability(itemStack)) return false;

        consumeOrDamageItem(entityBase, heldEquipment, drop);

        return true;
    }
}
