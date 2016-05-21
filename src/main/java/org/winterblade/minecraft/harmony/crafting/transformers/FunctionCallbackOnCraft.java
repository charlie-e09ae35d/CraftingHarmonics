package org.winterblade.minecraft.harmony.crafting.transformers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.crafting.events.ItemOnCraftedCallback;
import org.winterblade.minecraft.harmony.crafting.events.ItemOnCraftedEvent;
import org.winterblade.minecraft.harmony.crafting.events.wrappers.ItemStackWrapper;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.utility.SynchronizedRandom;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Created by Matt on 4/13/2016.
 */
@Component(properties = {"onCraft"})
public class FunctionCallbackOnCraft implements IItemStackTransformer {
    private final ItemOnCraftedCallback fn;

    public FunctionCallbackOnCraft(ItemOnCraftedCallback fn) throws OperationException {
        this.fn = fn;
    }

    @Override
    public ItemStack transform(ItemStack input, EntityPlayer craftingPlayer) {
        Random random = craftingPlayer.getEntityWorld().isRemote
                ? SynchronizedRandom.getMyRandom()
                : SynchronizedRandom.getRandomFor(craftingPlayer);

        ItemStackWrapper wrapper = new ItemStackWrapper(input);
        try {
            fn.apply(new ItemOnCraftedEvent(random, wrapper));
        } catch(Exception ex) {
            LogHelper.error("Error with onCraft callback function", ex);
        }

        // Return the now modified input:
        return wrapper.getItemStack();
    }

    @Nonnull
    @Override
    public IItemStackTransformer[] getImpliedTransformers() {
        return new IItemStackTransformer[0];
    }
}


