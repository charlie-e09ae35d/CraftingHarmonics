package org.winterblade.minecraft.harmony.mobs;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.mobs.dto.MobDrop;
import org.winterblade.minecraft.harmony.utility.LogHelper;

import java.util.*;

/**
 * Created by Matt on 5/4/2016.
 */
public class MobDropRegistry {
    private static final Map<UUID, DropHandler> handlers = new HashMap<>();
    private static final Set<UUID> activeHandlers = new HashSet<>();

    /**
     * Handles a mob drop event
     * @param evt
     */
    public static void handleDrops(LivingDropsEvent evt) {
        String entityName = evt.getEntity().getName();
        String entityClassName = evt.getEntity().getClass().getSimpleName();
        LogHelper.debug("Processing drops for '" + entityName + "' ('" + entityClassName + "').");

        for(UUID id : activeHandlers) {
            DropHandler handler = handlers.get(id);

            // If we don't have a handler, or the handler doesn't match:
            if(handler == null ||
                    (!entityName.equals(handler.getWhat()) && !entityClassName.equals(handler.getWhat()))) continue;

            // Check if we're replacing drops, and deal with it:
            if(handler.isReplace()) {
                // If we're not excluding items, just clear it all
                if(handler.getExcludes() == null || handler.getExcludes().length <= 0) {
                    evt.getDrops().clear();
                } else {
                    // Otherwise, sort through it
                    for (Iterator<EntityItem> iterator = evt.getDrops().iterator(); iterator.hasNext(); ) {
                        EntityItem drop = iterator.next();
                        ItemStack item = drop.getEntityItem();

                        boolean exclude = false;

                        // See if we should exclude it:
                        for(ItemStack excludedItem : handler.getExcludes()) {
                            if(item.isItemEqualIgnoreDurability(excludedItem)) continue;

                            exclude = true;
                            break;
                        }

                        // If we can exclude this, then do so, otherwise remove it:
                        if(exclude) continue;
                        iterator.remove();
                    }
                }
            }

            // If we're doing player only drops
            if(handler.isPlayerOnly() && !evt.getSource().damageType.equals("player")) continue;

            // Now, actually calculate out our drop rates...
            Random rand = evt.getEntity().getEntityWorld().rand;
            for(MobDrop drop : handler.getDrops()) {
                double dr = rand.nextDouble();

                // If we rolled higher than the chance, move on:
                if(drop.getChance() < dr) continue;

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
                dropStack.stackSize = qty;

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

    public static UUID registerHandler(String what, MobDrop[] drops, boolean replace, ItemStack[] exclude, boolean playerOnly) {
        UUID id = UUID.randomUUID();
        handlers.put(id, new DropHandler(what, drops, replace, exclude, playerOnly));
        return id;
    }

    public static void apply(UUID ticket) {
        activeHandlers.add(ticket);
    }

    public static void remove(UUID ticket) {
        activeHandlers.remove(ticket);
    }

    private static class DropHandler {
        private final String what;
        private final MobDrop[] drops;
        private final boolean replace;
        private final ItemStack[] exclude;
        private final boolean playerOnly;

        DropHandler(String what, MobDrop[] drops, boolean replace, ItemStack[] exclude, boolean playerOnly) {
            this.what = what;
            this.drops = drops;
            this.replace = replace;
            this.exclude = exclude;
            this.playerOnly = playerOnly;
        }

        public String getWhat() {
            return what;
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

        public boolean isPlayerOnly() {
            return playerOnly;
        }
    }
}
