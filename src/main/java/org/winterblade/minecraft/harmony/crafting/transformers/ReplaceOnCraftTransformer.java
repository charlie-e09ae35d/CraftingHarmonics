package org.winterblade.minecraft.harmony.crafting.transformers;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;

/**
 * Created by Matt on 4/10/2016.
 */
public class ReplaceOnCraftTransformer implements IItemStackTransformer {
    private final ItemStack replacement;

    public ReplaceOnCraftTransformer(ItemStack replacement) {
        this.replacement = replacement;
    }

    @Override
    public ItemStack transform(ItemStack input) {
        if(input.stackSize > 1) {
            // TODO
            return input;
        } else {
            replacement.stackSize++;
            return replacement;
        }
    }
}
