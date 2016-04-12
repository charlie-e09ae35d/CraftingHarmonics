package org.winterblade.minecraft.harmony.crafting.transformers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;

import javax.annotation.Nonnull;

/**
 * Created by Matt on 4/10/2016.
 */
@Component(properties = {"returnOnCraft"})
public class ReturnOnCraftTransformer implements IItemStackTransformer {
    private final boolean shouldReturn;

    public ReturnOnCraftTransformer() { shouldReturn = true; }

    public ReturnOnCraftTransformer(boolean shouldReturn) {
        this.shouldReturn = shouldReturn;
    }

    @Override
    public ItemStack transform(ItemStack input, EntityPlayer craftingPlayer) {
        if(!shouldReturn) return input;

        // TODO: Update inventory somehow.
        input.stackSize = input.stackSize + 1;
        return input;
    }

    @Nonnull
    @Override
    public IItemStackTransformer[] getImpliedTransformers() {
        return new IItemStackTransformer[0];
    }
}
