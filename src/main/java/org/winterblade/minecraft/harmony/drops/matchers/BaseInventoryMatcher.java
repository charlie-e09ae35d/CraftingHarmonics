package org.winterblade.minecraft.harmony.drops.matchers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Map;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseInventoryMatcher extends BaseItemStackMatcher {
    private final ItemStack requiredItem;

    public BaseInventoryMatcher(ItemStack requiredItem, double damagePer, boolean consume, EnumHand hand) {
        super(damagePer, consume, hand);
        this.requiredItem = requiredItem;
    }

    protected boolean matches(Entity entity, ItemStack drop) {
        if(entity == null || !EntityPlayer.class.isAssignableFrom(entity.getClass())) return false;

        // Get our entity and convert it over:
        EntityPlayer player = (EntityPlayer) entity;

        // Sanity checking...
        if(player.inventory == null || player.inventory.mainInventory == null) return false;

        // Find everything in the inventory that can match this...
        BiMap<Integer, ItemStack> matchingItems = HashBiMap.create();

        ItemStack[] mainInventory = player.inventory.mainInventory;
        for (int i = 0, mainInventoryLength = mainInventory.length; i < mainInventoryLength; i++) {
            ItemStack item = mainInventory[i];
            if(item == null || !item.isItemEqualIgnoreDurability(requiredItem)) continue;

            // If we only wanted to match the item, just say we found it here and save a lot of cycles:
            if(!consume && damagePer <= 0) return true;

            matchingItems.put(i, item);
        }

        int dropsRemaining = drop.stackSize;
        for(Map.Entry<Integer, ItemStack> entry : matchingItems.entrySet()) {
            ItemStack equipment = entry.getValue();
            int dropCount = 0;
            // Yes, there's code duplication here...
            if(consume) {
                // Figure out which we need to limit by:
                dropCount = Math.min(dropsRemaining, equipment.stackSize);

                // Decrement our held stack:
                equipment.stackSize -= dropCount;

                // Destroy the item if necessary:
                if(equipment.stackSize <= 0) {
                    equipment = null;
                }
            } else if(0 < damagePer) {
                // TODO: Deal with unbreaking enchants?
                int remainingDmg = equipment.getMaxDamage() - equipment.getItemDamage();
                int dropsAllowed = (int)(remainingDmg / damagePer);

                // Figure out which we need to limit by:
                dropCount = Math.min(dropsRemaining, dropsAllowed);

                // Decrement our held stack:
                equipment.setItemDamage(equipment.getItemDamage() + (int)(dropCount * damagePer));

                // Destroy the offhand if necessary:
                if(equipment.getMaxDamage() <= equipment.getItemDamage()) {
                    ForgeEventFactory.onPlayerDestroyItem(player, equipment, null);
                    equipment = null;
                }
            }

            player.replaceItemInInventory(entry.getKey(), equipment);
            dropsRemaining -= dropCount;

            // If we processed everything, bail
            if(dropsRemaining <= 0) break;
        }

        // If we couldn't generate all the items...
        if(0 < dropsRemaining) drop.stackSize -= dropsRemaining;

        return true;
    }
}
