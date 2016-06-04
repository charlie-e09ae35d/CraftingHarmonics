package org.winterblade.minecraft.harmony.common.matchers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.common.ItemUtility;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseInventoryMatcher extends BaseItemStackMatcher {
    private final ItemStack requiredItem;
    private final NBTTagCompound nbt;
    private final boolean fuzzyNbt;

    public BaseInventoryMatcher(ItemStack requiredItem, double damagePer, boolean consume, @Nullable NBTTagCompound nbt,
                                boolean fuzzyNbt, EnumHand hand) {
        super(damagePer, consume, hand);
        this.requiredItem = requiredItem;
        this.nbt = nbt;
        this.fuzzyNbt = fuzzyNbt;
    }

    protected BaseMatchResult matches(Entity entity, ItemStack drop) {
        if(entity == null || !EntityPlayer.class.isAssignableFrom(entity.getClass())) return BaseMatchResult.False;

        // Get our entity and convert it over:
        EntityPlayer player = (EntityPlayer) entity;

        // Sanity checking...
        if(player.inventory == null || player.inventory.mainInventory == null) return BaseMatchResult.False;

        // Only search through the player's main inventory:
        return matches(player.inventory, drop, 0, 36, true);
    }

    protected BaseMatchResult matches(TileEntity tileEntity, ItemStack drop, int min, int max, boolean partial) {
        if(!IInventory.class.isAssignableFrom(tileEntity.getClass())) return BaseMatchResult.False;
        return matches((IInventory)tileEntity, drop, min, max, partial);
    }

    /**
     * Find all items in the inventory matching the required item
     * @param inventory      The inventory to search through
     * @param drop           The drop to modify (if any)
     * @param lowerBound     The lower bound to search from
     * @param upperBound     The upper bound to search to
     * @param partial        If the matcher should match on a partial stack
     * @return               The match result.
     */
    private BaseMatchResult matches(IInventory inventory, @Nullable ItemStack drop, int lowerBound, int upperBound, boolean partial) {
        // Find everything in the inventory that can match this...
        BiMap<Integer, ItemStack> matchingItems = HashBiMap.create();

        for (int i = lowerBound, len = Math.min(inventory.getSizeInventory(),upperBound); i < len; i++) {
            ItemStack item = inventory.getStackInSlot(i);
            if(item == null
                    || !item.isItemEqualIgnoreDurability(requiredItem)
                    || (nbt != null && !ItemUtility.checkIfNbtMatches(nbt, item.getTagCompound(), fuzzyNbt))) continue;

            // If we only wanted to match the item, just say we found it here and save a lot of cycles:
            if(!consume && damagePer <= 0) return BaseMatchResult.True;

            matchingItems.put(i, item);
        }

        // If we didn't match anything...
        if(matchingItems.size() <= 0) return BaseMatchResult.False;

        Map<Integer, ItemStack> affectedItems = new HashMap<>();

        int dropsRemaining = drop != null ? drop.stackSize : Integer.MAX_VALUE;
        for(Map.Entry<Integer, ItemStack> entry : matchingItems.entrySet()) {
            ItemStack equipment = ItemStack.copyItemStack(entry.getValue());
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

                affectedItems.put(entry.getKey(), equipment);
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
                    equipment = null;
                }

                affectedItems.put(entry.getKey(), equipment);
            }

            dropsRemaining -= dropCount;

            // If we processed everything, bail
            if(dropsRemaining <= 0) break;
        }

        // If we couldn't generate all the items...
        if(0 < dropsRemaining && !partial) return BaseMatchResult.False;
        if(drop != null && 0 < dropsRemaining) drop.stackSize -= dropsRemaining;

        // Return something to update the inventory if we succeed.
        return new BaseMatchResult(true, new Runnable() {
            @Override
            public void run() {
                for(Map.Entry<Integer, ItemStack> entry : affectedItems.entrySet()) {
                    inventory.setInventorySlotContents(entry.getKey(), entry.getValue());
                }
            }
        });
    }
}
