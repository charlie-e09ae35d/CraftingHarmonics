package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/7/2016.
 */
public abstract class BaseItemStackMatcher {
    protected final boolean consume;
    protected final double damagePer;
    protected final EnumHand hand;

    public BaseItemStackMatcher(double damagePer, boolean consume, EnumHand hand) {
        this.damagePer = damagePer;
        this.consume = consume;
        this.hand = hand;
    }

    @Nullable
    protected Runnable consumeOrDamageItem(EntityLivingBase entityBase, ItemStack equipment, ItemStack drop) {
        if(consume) {
            // Figure out which we need to limit by:
            int dropCount = Math.min(drop.stackSize, equipment.stackSize);

            // Decrement our drop size if we need to:
            if(dropCount < drop.stackSize) drop.stackSize = dropCount;

            // Return the update  to actually affect this:
            return () -> {
                // Decrement our held stack:
                equipment.stackSize -= dropCount;

                // Destroy the item if necessary:
                if(equipment.stackSize <= 0 && hand != null) {
                    entityBase.setHeldItem(hand, null);
                }
            };
        }

        if(0 < damagePer) {
            // TODO: Deal with unbreaking enchants?
            int remainingDmg = equipment.getMaxDamage() - equipment.getItemDamage();
            int dropsAllowed = (int)(remainingDmg / damagePer);

            // Figure out which we need to limit by:
            int dropCount = Math.min(drop.stackSize, dropsAllowed);

            // Decrement our drop size if we need to:
            if(dropCount < drop.stackSize) drop.stackSize = dropCount;

            return () -> {
                // Decrement our held stack:
                equipment.setItemDamage(equipment.getItemDamage() + (int) (dropCount * damagePer));

                // Destroy the offhand if necessary:
                if (equipment.getMaxDamage() <= equipment.getItemDamage()) {
                    if (hand != null) entityBase.setHeldItem(hand, null);

                    // And fire the event if necessary:
                    if (EntityPlayer.class.isAssignableFrom(entityBase.getClass())) {
                        ForgeEventFactory.onPlayerDestroyItem((EntityPlayer) entityBase, equipment, hand);
                    }
                }
            };
        }

        return null;
    }
}
