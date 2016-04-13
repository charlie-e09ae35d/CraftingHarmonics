package org.winterblade.minecraft.harmony.crafting.transformers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.events.ItemOnCraftedCallback;
import org.winterblade.minecraft.harmony.crafting.events.ItemOnCraftedEvent;
import org.winterblade.minecraft.harmony.crafting.events.wrappers.ItemStackWrapper;
import org.winterblade.minecraft.harmony.utility.SynchronizedRandom;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Created by Matt on 4/13/2016.
 */
@Component(properties = {"onCraft"})
public class FunctionCallbackOnCraft implements IItemStackTransformer {
    private final ItemOnCraftedCallback fn;

    public FunctionCallbackOnCraft(ItemOnCraftedCallback fn) throws ItemMissingException {
        this.fn = fn;
    }

    @Override
    public ItemStack transform(ItemStack input, EntityPlayer craftingPlayer) {
        Random random = craftingPlayer.getEntityWorld().isRemote
                ? SynchronizedRandom.getRandomFor(craftingPlayer)
                : SynchronizedRandom.getMyRandom();

        ItemStackWrapper wrapper = new ItemStackWrapper(input);
        try {
            fn.apply(new ItemOnCraftedEvent(random, wrapper));
        } catch(Exception ex) {
            System.err.println("Error with onCraft callback function: " + ex.getMessage());
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


