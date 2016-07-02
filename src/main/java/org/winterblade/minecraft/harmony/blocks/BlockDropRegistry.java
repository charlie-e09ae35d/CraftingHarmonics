package org.winterblade.minecraft.harmony.blocks;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.blocks.drops.BlockDrop;
import org.winterblade.minecraft.harmony.common.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.common.blocks.BlockStateMatcher;
import org.winterblade.minecraft.harmony.common.drops.BaseDropHandler;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Matt on 5/12/2016.
 */
public class BlockDropRegistry {
    private static final Map<UUID, DropHandler> handlers = new HashMap<>();
    private static final Set<UUID> activeHandlers = new LinkedHashSet<>();
    private static final Set<BlockPos> explodedBlocks = new HashSet<>();
    private static final LoadingCache<IBlockState, Set<UUID>> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .build(new CacheLoader<IBlockState, Set<UUID>>() {
        @Override
        public Set<UUID> load(IBlockState key) throws Exception {
            if(CraftingHarmonicsMod.getConfigManager().debugBlockDropEvents()) {
                LogHelper.info("Generating block drop handler cache for '{}' ('{}').",
                        Block.REGISTRY.getNameForObject(key.getBlock()).toString(),
                        key.toString());
            }

            return activeHandlers.stream().filter(id -> {
                DropHandler handler = handlers.get(id);
                return handler != null
                        && handler.isMatch(Block.REGISTRY.getNameForObject(key.getBlock()).toString(), key);
            }).collect(Collectors.toSet());
        }
    });

    public static void handleDrops(BlockEvent.HarvestDropsEvent evt) {
        IBlockState state = evt.getState();

        if(CraftingHarmonicsMod.getConfigManager().debugBlockDropEvents()) {
            Block block = state.getBlock();
            String blockName = Block.REGISTRY.getNameForObject(block).toString();
            LogHelper.info("Processing drops for '" + blockName + "' ('" + state.toString() + "').");
        }

        // Get our matching IDs:
        Set<UUID> ids;
        try {
            // This will run through the cache if we haven't run this yet...
            ids = cache.get(state);
        } catch (ExecutionException e) {
            LogHelper.warn("Unable to process drops for this block.", e);
            return;
        }

        for(UUID id : ids) {
            DropHandler handler = handlers.get(id);

            // If we don't have a handler:
            if(handler == null) continue;

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
                if(drop == null) continue;

                ItemStack dropStack;
                BaseMatchResult result;

                do {
                    int min = drop.getMin();
                    int max = drop.getMax();

                    // Figure out how many to give:
                    int qty;
                    if (min != max) {
                        int delta = Math.abs(drop.getMax() - drop.getMin());
                        qty = rand.nextInt(delta) + min;
                    } else {
                        qty = min;
                    }

                    // Do the drop!
                    dropStack = ItemStack.copyItemStack(drop.getWhat());

                    // Update the stack size:
                    try {
                        dropStack.stackSize = Math.toIntExact(qty + Math.round(evt.getFortuneLevel() * drop.getFortuneMultiplier()));
                    } catch (ArithmeticException e) {
                        // You'd have to try really hard to do this, but... just in case...
                        dropStack.stackSize = 64;
                    }

                    // Check if this drop matches:
                    result = drop.matches(evt, dropStack);
                    if(result.isMatch()) break;
                    drop = (BlockDrop) drop.getAltMatch();
                } while(drop != null);

                if(!result.isMatch() || drop == null) continue;

                // Make sure we have sane drop amounts:
                if(dropStack.stackSize < 0) continue;
                if(dropStack.getMaxStackSize() < dropStack.stackSize) dropStack.stackSize = dropStack.getMaxStackSize();

                // Now perform our updates:
                if(result.getCallback() != null) result.getCallback().run();

                // Run any entity callbacks we have:
                IEntityCallback[] callbacks = drop.getOnDrop();
                if(callbacks != null && 0 < callbacks.length && evt.getHarvester() != null) {
                    MobTickRegistry.addCallbackSet(evt.getHarvester(), callbacks, new BaseEntityMatcherData(evt.getHarvester()));
                }

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
        cache.invalidateAll();
    }

    public static void remove(UUID ticket) {
        activeHandlers.remove(ticket);
        cache.invalidateAll();
    }

    /**
     * Called to register that a block is about to be exploded
     * @param affectedBlocks    The blocks to be exploded
     */
    public static void registerExplodedBlocks(List<BlockPos> affectedBlocks) {
        explodedBlocks.addAll(affectedBlocks);
    }

    /**
     * Checks if the given position has been exploded recently
     * @param pos    The position to check
     * @return       True if this was exploded, false otherwise
     */
    public static boolean wasExploded(BlockPos pos) {
        return explodedBlocks.contains(pos);
    }

    /**
     * Called once we're done with the tick in which we should process explosions
     */
    public static void clearExplodedList() {
        explodedBlocks.clear();
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
