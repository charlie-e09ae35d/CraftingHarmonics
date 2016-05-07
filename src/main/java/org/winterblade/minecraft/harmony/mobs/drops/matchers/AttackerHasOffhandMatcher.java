package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;

/**
 * Created by Matt on 5/7/2016.
 */
@Component(properties = {"attackerHasOffhand", "consume", "damagePer"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class AttackerHasOffhandMatcher extends BaseItemStackMatcher {
    private final ItemStack offhand;

    public AttackerHasOffhandMatcher(ItemStack offhand) {
        this(offhand, false);
    }

    public AttackerHasOffhandMatcher(ItemStack offhand, boolean consume) {
        this(offhand, consume, 0);
    }

    public AttackerHasOffhandMatcher(ItemStack offhand, boolean consume, double damagePer) {
        super(damagePer, consume, EnumHand.OFF_HAND);
        this.offhand = offhand;
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

        consumeOrDamageItem(entityBase, heldEquipment, drop);

        return true;
    }

}
