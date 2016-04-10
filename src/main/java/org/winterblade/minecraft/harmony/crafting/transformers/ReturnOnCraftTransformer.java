package org.winterblade.minecraft.harmony.crafting.transformers;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;

/**
 * Created by Matt on 4/10/2016.
 */
public class ReturnOnCraftTransformer implements IItemStackTransformer {
    @Override
    public ItemStack transform(ItemStack input) {
        input.stackSize = input.stackSize + 1;
        return input;
    }
}
