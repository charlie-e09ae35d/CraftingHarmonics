package org.winterblade.minecraft.harmony.crafting.transformers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
            if(!craftingPlayer.inventory.addItemStackToInventory(output)) {
                craftingPlayer.dropItem(output, false, false);
            }
            return input;
        } else {
            output.stackSize++;
            return output;
        }
    }
}
