package org.winterblade.minecraft.harmony.mobs;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.drops.BaseDropHandler;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.mobs.sheds.MobShed;

import java.util.*;

/**
 * Created by Matt on 5/10/2016.
 */
public class MobShedRegistry {
    private MobShedRegistry() {}

    private static final Map<UUID, MobShedHandler> handlers = new HashMap<>();
    private static final Set<UUID> activeHandlers = new LinkedHashSet<>();
    private static boolean isActive = false;

    /**
     * Handles a mob shed event
     * @param evt    The event to process
     */
    public static void handleSheds(TickEvent.WorldTickEvent evt) {
        // Only process sheds once every 10 seconds (roughly):
        if(!isActive || evt.world.getTotalWorldTime() % CraftingHarmonicsMod.getConfigManager().getShedSeconds() != 0)
            return;

        Random rand = evt.world.rand;
        // Doing it this way to avoid doing modulo for potentially thousands of entities in a LivingUpdate event.
        List<EntityLiving> entities = evt.world.getEntities(EntityLiving.class, EntitySelectors.IS_ALIVE);

        for(EntityLiving entity : entities) {
            if (EntityPlayer.class.isAssignableFrom(entity.getClass())) continue;

            String entityName = entity.getName();
            String entityClassName = entity.getClass().getName();

            for (UUID id : activeHandlers) {
                MobShedHandler handler = handlers.get(id);

                // If we don't have a handler, or the handler doesn't match:
                if (handler == null ||
                        (!handler.isMatch(entityName)) && !handler.isMatch(entityClassName)) continue;

                // Now, actually calculate out our drop rates...
                for (MobShed drop : handler.getDrops()) {
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
                    ItemStack dropStack = ItemStack.copyItemStack(drop.getWhat());

                    // Check if this drop matches:
                    BaseMatchResult result = drop.matches(entity, dropStack);
                    if(!result.isMatch()) continue;

                    // Make sure we have sane drop amounts:
                    if (dropStack.stackSize < 0) continue;
                    if (dropStack.getMaxStackSize() < dropStack.stackSize)
                        dropStack.stackSize = dropStack.getMaxStackSize();

                    // Now perform our updates:
                    if(result.getCallback() != null) result.getCallback().run();

                    // Do the drop:
                    entity.entityDropItem(dropStack, 0.0f);
                }
            }
        }
    }

    public static UUID registerHandler(String[] what, MobShed[] drops) {
        UUID id = UUID.randomUUID();
        handlers.put(id, new MobShedHandler(what, drops));
        return id;
    }

    public static void apply(UUID ticket) {
        isActive = true;
        activeHandlers.add(ticket);
    }

    public static void remove(UUID ticket) {
        activeHandlers.remove(ticket);
        if(activeHandlers.size() <= 0) isActive = false;
    }

    private static class MobShedHandler extends BaseDropHandler<MobShed> {
        MobShedHandler(String[] what, MobShed[] drops) {
            super(what, drops);
        }
    }
}
