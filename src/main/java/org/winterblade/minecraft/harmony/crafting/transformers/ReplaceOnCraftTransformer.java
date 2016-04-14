package org.winterblade.minecraft.harmony.crafting.transformers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

import javax.annotation.Nonnull;

/**
 * Created by Matt on 4/10/2016.
 */
@Component(properties = {"replaceOnCraft"})
public class ReplaceOnCraftTransformer implements IItemStackTransformer {
    private final ItemStack replacement;

    public ReplaceOnCraftTransformer(ItemStack replacement) {
        this.replacement = replacement;
    }

    @Override
    public ItemStack transform(ItemStack input, EntityPlayer craftingPlayer) {
        ItemStack output = replacement.copy();
        return output;
    }

    @Nonnull
    @Override
    public IItemStackTransformer[] getImpliedTransformers() {
        return new IItemStackTransformer[0];
    }
}
