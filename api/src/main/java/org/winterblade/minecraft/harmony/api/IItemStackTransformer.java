package org.winterblade.minecraft.harmony.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by Matt on 4/10/2016.
 */
public interface IItemStackTransformer {
    ItemStack transform(ItemStack input, EntityPlayer craftingPlayer);
    @Nonnull IItemStackTransformer[] getImpliedTransformers();
}
