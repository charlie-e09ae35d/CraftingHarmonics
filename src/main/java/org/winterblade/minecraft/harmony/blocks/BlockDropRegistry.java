package org.winterblade.minecraft.harmony.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.blocks.drops.BlockDrop;
import org.winterblade.minecraft.harmony.drops.BaseDropHandler;
import org.winterblade.minecraft.harmony.utility.LogHelper;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Matt on 5/12/2016.
 */
public class BlockDropRegistry {
    private static final Map<UUID, DropHandler> handlers = new HashMap<>();
    private static final Set<UUID> activeHandlers = new LinkedHashSet<>();


    public static void handleDrops(BlockEvent.HarvestDropsEvent evt) {
        IBlockState state = evt.getState();
        Block block = state.getBlock();

        String blockName = Block.REGISTRY.getNameForObject(block).toString();

        if(CraftingHarmonicsMod.getConfigManager().debugBlockDropEvents()) {
            LogHelper.info("Processing drops for '" + blockName + "' ('" + state.toString() + "').");
        }

        // TODO: Cache these instead of running through every one
        for(UUID id : activeHandlers) {
            DropHandler handler = handlers.get(id);

            // If we don't have a handler, or the handler doesn't match:
            if(handler == null || !handler.isMatch(blockName, state)) continue;

            // Check if we're replacing drops, and deal with it:
            if(handler.isReplace() || 0 < handler.getRemovals().length) {
                // If we're replacing everything, and not excluding items, just clear it all
                if(handler.isReplace() && (handler.getExcludes() == null || handler.getExcludes().length <= 0)) {
                    evt.getDrops().clear();
                } else {
                    // Otherwise, sort through it
                    for (Iterator<ItemStack> iterator = evt.getDrops().iterator(); iterator.hasNext(); ) {
                        ItemStack item = iterator.next();

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
            Random rand = evt.getWorld().rand;
            for(BlockDrop drop : handler.getDrops()) {
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
                    dropStack.stackSize = Math.toIntExact(qty + Math.round(evt.getFortuneLevel() * drop.getFortuneMultiplier()));
                } catch(ArithmeticException e) {
                    // You'd have to try really hard to do this, but... just in case...
                    dropStack.stackSize = 64;
                }

                // Check if this drop matches:
                BaseDropMatchResult result = drop.matches(evt, dropStack);
                if(!result.isMatch()) continue;

                // Make sure we have sane drop amounts:
                if(dropStack.stackSize < 0) continue;
                if(dropStack.getMaxStackSize() < dropStack.stackSize) dropStack.stackSize = dropStack.getMaxStackSize();

                // Now perform our updates:
                if(result.getCallback() != null) result.getCallback().run();

                evt.getDrops().add(dropStack);
                evt.setDropChance(1.0f); // If we had a drop, go ahead and take over the event.
            }
        }
    }

    public static UUID registerHandler(String[] what, BlockDrop[] drops, boolean replace, ItemStack[] exclude,
                                       ItemStack[] remove, BlockStateMatcher blockStateMatcher) {
        UUID id = UUID.randomUUID();
        handlers.put(id, new DropHandler(what, drops, replace, exclude, remove, blockStateMatcher));
        return id;
    }

    public static void apply(UUID ticket) {
        activeHandlers.add(ticket);
    }

    public static void remove(UUID ticket) {
        activeHandlers.remove(ticket);
    }

    private static class DropHandler extends BaseDropHandler<BlockDrop> {
        private final ItemStack[] remove;
        private final boolean replace;
        private final ItemStack[] exclude;
        @Nullable
        private final BlockStateMatcher blockStateMatcher;

        DropHandler(String[] what, BlockDrop[] drops, boolean replace, ItemStack[] exclude, ItemStack[] remove,
                    @Nullable BlockStateMatcher blockStateMatcher) {
            super(what, drops);
            this.replace = replace;
            this.exclude = exclude != null ? exclude : new ItemStack[0];
            this.remove = remove != null ? remove : new ItemStack[0];
            this.blockStateMatcher = blockStateMatcher;
        }

        boolean isReplace() {
            return replace;
        }

        ItemStack[] getExcludes() {
            return exclude;
        }

        ItemStack[] getRemovals() {
            return remove;
        }

        public boolean isMatch(String blockName, IBlockState state) {
            return isMatch(blockName) && (blockStateMatcher == null || blockStateMatcher.matches(state));
        }
    }
}
