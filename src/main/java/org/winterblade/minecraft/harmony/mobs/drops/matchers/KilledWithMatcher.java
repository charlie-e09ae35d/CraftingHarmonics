package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;

/**
 * Created by Matt on 5/7/2016.
 */
@Component(properties = {"killedWith"})
@PrioritizedObject(priority = Priority.HIGH)
public class KilledWithMatcher implements IMobDropMatcher {
    private final ItemStack killedWith;

    public KilledWithMatcher(ItemStack killedWith) {
        this.killedWith = killedWith;
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @return True if it should match; false otherwise
     */
    @Override
    public boolean isMatch(LivingDropsEvent evt, ItemStack drop) {
        Entity entity = evt.getSource().getEntity();
        if(entity == null || !EntityLivingBase.class.isAssignableFrom(entity.getClass())) return false;

        // Get our entity and convert it over:
        EntityLivingBase entityBase = (EntityLivingBase) evt.getSource().getEntity();
        if(entityBase == null) return false;

        ItemStack heldEquipment = entityBase.getHeldItemMainhand();

        // Make sure we have held equipment and that it's right:
        return heldEquipment != null && heldEquipment.isItemEqualIgnoreDurability(killedWith);
    }
}
