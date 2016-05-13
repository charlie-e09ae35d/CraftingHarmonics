package org.winterblade.minecraft.harmony.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.blocks.drops.BlockDrop;
import org.winterblade.minecraft.harmony.drops.BaseDropHandler;
import org.winterblade.minecraft.harmony.mobs.drops.MobDrop;

import java.util.*;

/**
 * Created by Matt on 5/12/2016.
 */
public class BlockDropRegistry {
    private static final Map<UUID, DropHandler> handlers = new HashMap<>();
    private static final Set<UUID> activeHandlers = new LinkedHashSet<>();


    public static void handleDrops(BlockEvent.HarvestDropsEvent evt) {
        // TODO.
    }

    public static UUID registerHandler(String[] what, BlockDrop[] drops, boolean replace, ItemStack[] exclude,
                                       ItemStack[] remove) {
        UUID id = UUID.randomUUID();
        handlers.put(id, new DropHandler(what, drops, replace, exclude, remove));
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

        DropHandler(String[] what, BlockDrop[] drops, boolean replace, ItemStack[] exclude, ItemStack[] remove) {
            super(what, drops);
            this.replace = replace;
            this.exclude = exclude != null ? exclude : new ItemStack[0];
            this.remove = remove != null ? remove : new ItemStack[0];
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
    }
}
