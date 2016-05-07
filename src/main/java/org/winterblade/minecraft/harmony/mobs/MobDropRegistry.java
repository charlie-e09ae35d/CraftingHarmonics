package org.winterblade.minecraft.harmony.mobs;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.mobs.drops.MobDrop;
import org.winterblade.minecraft.harmony.utility.LogHelper;

import java.util.*;

/**
 * Created by Matt on 5/4/2016.
 */
public class MobDropRegistry {
    private static final Map<UUID, DropHandler> handlers = new HashMap<>();
    private static final Set<UUID> activeHandlers = new LinkedHashSet<>();

    /**
     * Handles a mob drop event
     * @param evt
     */
    public static void handleDrops(LivingDropsEvent evt) {
        String entityName = evt.getEntity().getName();
        String entityClassName = evt.getEntity().getClass().getName();

        if(CraftingHarmonicsMod.getConfigManager().debugMobDropEvents()) {
            LogHelper.info("Processing drops for '" + entityName + "' ('" + entityClassName + "') from damageType '"
                    + evt.getSource().getDamageType() + "'.");
        }

        for(UUID id : activeHandlers) {
            DropHandler handler = handlers.get(id);

            // If we don't have a handler, or the handler doesn't match:
            if(handler == null ||
                    (!handler.isMatch(entityName)) && !handler.isMatch(entityClassName)) continue;

            // If this is a player and we shouldn't be handling player drops...
            if(!handler.includePlayerDrops() && evt.getEntity() instanceof EntityPlayer) continue;

            // Check if we're replacing drops, and deal with it:
            if(handler.isReplace() || 0 < handler.getRemovals().length) {
                // If we're replacing everything, and not excluding items, just clear it all
                if(handler.isReplace() && (handler.getExcludes() == null || handler.getExcludes().length <= 0)) {
                    evt.getDrops().clear();
                } else {
                    // Otherwise, sort through it
                    for (Iterator<EntityItem> iterator = evt.getDrops().iterator(); iterator.hasNext(); ) {
                        EntityItem drop = iterator.next();
                        ItemStack item = drop.getEntityItem();

                        boolean remove = handler.isReplace();

                        // See if we should remove it:
                        for (ItemStack removedItem : handler.getRemovals()) {
                            if (!item.isItemEqualIgnoreDurability(removedItem)) continue;

                            remove = true;
                            break;
                        }

                        // If we're removing, see if we should exclude it:
                        if(remove) {
                            for (ItemStack excludedItem : handler.getExcludes()) {
                                if (!item.isItemEqualIgnoreDurability(excludedItem)) continue;

                                remove = false;
                                break;
                            }
                        }

                        // If we can should remove this, then do so:
                        if(remove) iterator.remove();
                    }
                }
            }

            // Now, actually calculate out our drop rates...
            Random rand = evt.getEntity().getEntityWorld().rand;
            for(MobDrop drop : handler.getDrops()) {
                int min = drop.getMin();
                int max = drop.getMax();

                // Figure out how many to give:
                int qty;
                if(min != max) {
                    int delta = Math.abs(drop.getMax() - drop.getMin());
                    qty = rand.nextInt(delta) + min;
                } else {
                    qty = min;
                }

                // Do the drop!
                ItemStack dropStack = ItemStack.copyItemStack(drop.getWhat());

                // Update the stack size:
                try {
                    dropStack.stackSize = Math.toIntExact(qty + Math.round(evt.getLootingLevel() * drop.getLootingMultiplier()));
                } catch(ArithmeticException e) {
                    // You'd have to try really hard to do this, but... just in case...
                    dropStack.stackSize = 64;
                }

                // Check if this drop matches:
                if(!drop.matches(evt, dropStack)) continue;

                // Make sure we have sane drop amounts:
                if(dropStack.stackSize < 0) continue;
                if(dropStack.getMaxStackSize() < dropStack.stackSize) dropStack.stackSize = dropStack.getMaxStackSize();

                evt.getDrops().add(
                        new EntityItem(
                                evt.getEntity().getEntityWorld(),
                                evt.getEntity().posX,
                                evt.getEntity().posY,
                                evt.getEntity().posZ,
                                dropStack));
            }
        }
    }

    public static UUID registerHandler(String[] what, MobDrop[] drops, boolean replace, ItemStack[] exclude,
                                       ItemStack[] remove, boolean includePlayerDrops) {
        UUID id = UUID.randomUUID();
        handlers.put(id, new DropHandler(what, drops, replace, exclude, remove, includePlayerDrops));
        return id;
    }

    public static void apply(UUID ticket) {
        activeHandlers.add(ticket);
    }

    public static void remove(UUID ticket) {
        activeHandlers.remove(ticket);
    }

    private static class DropHandler {
        private final List<String> what;
        private final ItemStack[] remove;
        private final MobDrop[] drops;
        private final boolean replace;
        private final boolean includePlayerDrops;
        private final ItemStack[] exclude;

        DropHandler(String[] what, MobDrop[] drops, boolean replace, ItemStack[] exclude, ItemStack[] remove, boolean includePlayerDrops) {
            this.replace = replace;
            this.includePlayerDrops = includePlayerDrops;
            this.what = Lists.newArrayList(what);
            this.drops = drops != null ? drops : new MobDrop[0];
            this.exclude = exclude != null ? exclude : new ItemStack[0];
            this.remove = remove != null ? remove : new ItemStack[0];
        }

        public boolean isMatch(String entity) {
            return what == null || what.size() <= 0 || what.contains(entity);
        }

        public MobDrop[] getDrops() {
            return drops;
        }

        public boolean isReplace() {
            return replace;
        }

        public ItemStack[] getExcludes() {
            return exclude;
        }

        public ItemStack[] getRemovals() {
            return remove;
        }

        public boolean includePlayerDrops() {
            return includePlayerDrops;
        }
    }
}