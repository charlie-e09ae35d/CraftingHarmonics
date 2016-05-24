package org.winterblade.minecraft.harmony.mobs.sheds;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.common.drops.BaseDrop;

import java.util.Random;

/**
 * Created by Matt on 5/10/2016.
 */
public class MobShed extends BaseDrop<EntityLivingBase, IMobShedMatcher> {
    public static class Handler extends BaseEventMatch.BaseMatchHandler<MobShed> {
        /**
         * Applies the handler to the given mob
         * @param rand       The rand to use
         * @param entity     The entity to apply to
         */
        public void apply(Random rand, EntityLivingBase entity) {
            // Now, actually calculate out our drop rates...
            for (MobShed drop : this.getMatchers()) {
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
