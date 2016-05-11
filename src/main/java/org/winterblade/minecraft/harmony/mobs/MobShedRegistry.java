package org.winterblade.minecraft.harmony.mobs;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.winterblade.minecraft.harmony.mobs.sheds.MobShed;

import java.util.*;

/**
 * Created by Matt on 5/10/2016.
 */
public class MobShedRegistry {
    private MobShedRegistry() {}

    private static final Map<UUID, MobShedHandler> handlers = new HashMap<>();
    private static final Set<UUID> activeHandlers = new LinkedHashSet<>();

    /**
     * Handles a mob shed event
     * @param evt    The event to process
     */
    public static void handleSheds(LivingEvent.LivingUpdateEvent evt) {
        // Only process sheds once every 10 seconds (roughly):
        if(evt.getEntity().getEntityWorld().getTotalWorldTime() % 200 != 0)
            return;

        String entityName = evt.getEntity().getName();
        String entityClassName = evt.getEntity().getClass().getName();

        for(UUID id : activeHandlers) {
            MobShedHandler handler = handlers.get(id);

            // If we don't have a handler, or the handler doesn't match:
            if(handler == null ||
                    (!handler.isMatch(entityName)) && !handler.isMatch(entityClassName)) continue;

            // Now, actually calculate out our drop rates...
            Random rand = evt.getEntity().getEntityWorld().rand;

            for(MobShed drop : handler.getDrops()) {
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

                // Check if this drop matches:
                if(!drop.matches(evt, dropStack)) continue;

                // Make sure we have sane drop amounts:
                if(dropStack.stackSize < 0) continue;
                if(dropStack.getMaxStackSize() < dropStack.stackSize) dropStack.stackSize = dropStack.getMaxStackSize();

                // Do the drop:
                evt.getEntity().entityDropItem(dropStack, 0.0f);
            }

        }
    }

    public static UUID registerHandler(String[] what, MobShed[] drops) {
        UUID id = UUID.randomUUID();
        handlers.put(id, new MobShedHandler(what, drops));
        return id;
    }

    public static void apply(UUID ticket) {
        activeHandlers.add(ticket);
    }

    public static void remove(UUID ticket) {
        activeHandlers.remove(ticket);
    }

    private static class MobShedHandler extends BaseMobDropHandler<MobShed> {
        MobShedHandler(String[] what, MobShed[] drops) {
            super(what, drops);
        }
    }
}
