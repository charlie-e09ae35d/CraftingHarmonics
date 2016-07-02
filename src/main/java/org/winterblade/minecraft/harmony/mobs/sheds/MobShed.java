package org.winterblade.minecraft.harmony.mobs.sheds;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.common.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.common.drops.BaseDrop;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;

import java.util.Random;

/**
 * Created by Matt on 5/10/2016.
 */
public class MobShed extends BaseDrop<EntityLivingBase, IMobShedMatcher> {
    public static class Handler extends BaseEventMatch.BaseMatchHandler<MobShed, EntityLivingBase> {
        /**
         * Applies the handler to the given mob
         * @param rand       The rand to use
         * @param entity     The entity to apply to
         */
        public void apply(Random rand, EntityLivingBase entity) {
            // Now, actually calculate out our drop rates...
            for (MobShed drop : this.getMatchers()) {
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
                    dropStack.stackSize = qty;

                    // Check if this drop matches:
                    result = drop.matches(entity, dropStack);
                    if(result.isMatch()) break;
                    drop = (MobShed) drop.getAltMatch();
                } while(drop != null);
                if(!result.isMatch() || drop == null) continue;

                // Make sure we have sane drop amounts:
                if (dropStack.stackSize < 0) continue;
                if (dropStack.getMaxStackSize() < dropStack.stackSize)
                    dropStack.stackSize = dropStack.getMaxStackSize();

                // Now perform our updates:
                if(result.getCallback() != null) result.getCallback().run();

                // Run any entity callbacks we have:
                IEntityCallback[] callbacks = drop.getOnDrop();
                if(callbacks != null && 0 < callbacks.length) {
                    MobTickRegistry.addCallbackSet(entity, callbacks, new BaseEntityMatcherData(entity));
                }

                // Do the drop:
                entity.entityDropItem(dropStack, 0.0f);
            }
        }
    }
}
