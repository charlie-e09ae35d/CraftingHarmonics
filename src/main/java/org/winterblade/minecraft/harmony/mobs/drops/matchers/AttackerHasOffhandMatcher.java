package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;

/**
 * Created by Matt on 5/7/2016.
 */
@Component(properties = {"attackerHasOffhand", "consume", "damagePer"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class AttackerHasOffhandMatcher implements IMobDropMatcher {

    private final ItemStack offhand;
    private final boolean consume;
    private final double damagePer;

    public AttackerHasOffhandMatcher(ItemStack offhand) {
        this(offhand, false);
    }

    public AttackerHasOffhandMatcher(ItemStack offhand, boolean consume) {
        this(offhand, consume, 0);
    }

    public AttackerHasOffhandMatcher(ItemStack offhand, boolean consume, double damagePer) {
        this.offhand = offhand;
        this.consume = consume;
        this.damagePer = damagePer;
    }


    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt  The event to match
     * @param drop The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public boolean isMatch(LivingDropsEvent evt, ItemStack drop) {
        Entity entity = evt.getSource().getEntity();
        if(entity == null || !EntityLivingBase.class.isAssignableFrom(entity.getClass())) return false;

        // Get our entity and convert it over:
        EntityLivingBase entityBase = (EntityLivingBase) entity;

        ItemStack heldEquipment = entityBase.getHeldItemOffhand();

        // Make sure we have held equipment and that it's right:
        if(heldEquipment == null || !heldEquipment.isItemEqualIgnoreDurability(offhand)) return false;

        if(consume) {
            // Figure out which we need to limit by:
            int dropCount = Math.min(drop.stackSize, heldEquipment.stackSize);

            // Decrement our drop size if we need to:
            if(dropCount < drop.stackSize) drop.stackSize = dropCount;

            // Decrement our held stack:
            heldEquipment.stackSize -= dropCount;

            // Destroy the offhand if necessary:
            if(heldEquipment.stackSize <= 0) {
                entityBase.setHeldItem(EnumHand.OFF_HAND, null);
            }
        } else if(0 < damagePer) {
            // TODO: Deal with unbreaking enchants?
            int remainingDmg = heldEquipment.getMaxDamage() - heldEquipment.getItemDamage();
            int dropsAllowed = (int)(remainingDmg / damagePer);

            // Figure out which we need to limit by:
            int dropCount = Math.min(drop.stackSize, dropsAllowed);

            // Decrement our drop size if we need to:
            if(dropCount < drop.stackSize) drop.stackSize = dropCount;

            // Decrement our held stack:
            heldEquipment.setItemDamage(heldEquipment.getItemDamage() + (int)(dropCount * damagePer));

            // Destroy the offhand if necessary:
            if(heldEquipment.getMaxDamage() <= heldEquipment.getItemDamage()) {
                entityBase.setHeldItem(EnumHand.OFF_HAND, null);

                // And fire the event if necessary:
                if(EntityPlayer.class.isAssignableFrom(entity.getClass())) {
                    ForgeEventFactory.onPlayerDestroyItem((EntityPlayer) entityBase, heldEquipment, EnumHand.OFF_HAND);
                }
            }
        }

        return true;
    }
}
