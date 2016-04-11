package org.winterblade.minecraft.harmony.crafting.transformers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/10/2016.
 */
public class ReplaceOnCraftTransformer implements IItemStackTransformer {
    private final ItemStack replacement;

    public ReplaceOnCraftTransformer(ItemStack replacement) {
        this.replacement = replacement;
    }

    @Override
    public ItemStack transform(ItemStack input, EntityPlayer craftingPlayer) {
        ItemStack output = ItemRegistry.duplicate(replacement);
        if(input.stackSize > 1) {
            ItemHandlerHelper.giveItemToPlayer(craftingPlayer, output);
            return input;
        } else {
            output.stackSize++;
            return output;
        }
    }
}
