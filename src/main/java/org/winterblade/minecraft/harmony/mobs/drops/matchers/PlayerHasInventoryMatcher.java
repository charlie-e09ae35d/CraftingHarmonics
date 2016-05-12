package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseInventoryMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseItemStackMatcher;

import java.util.Map;

/**
 * Created by Matt on 5/7/2016.
 */
@Component(properties = {"playerHasInventory", "consumeInventory", "damageInventoryPer"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class PlayerHasInventoryMatcher extends BaseInventoryMatcher implements IMobDropMatcher {
    public PlayerHasInventoryMatcher(ItemStack requiredItem) {
        this(requiredItem, false);
    }

    public PlayerHasInventoryMatcher(ItemStack requiredItem, boolean consume) {
        this(requiredItem, consume, 0);
    }

    public PlayerHasInventoryMatcher(ItemStack requiredItem, boolean consume, double damagePer) {
        super(requiredItem, damagePer, consume, null);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt  The event to match
     * @param drop The dropped requiredItem; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        return matches(evt.getSource().getEntity(), drop);
    }
}
