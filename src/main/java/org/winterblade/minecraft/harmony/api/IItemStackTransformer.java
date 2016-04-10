package org.winterblade.minecraft.harmony.api;

import net.minecraft.item.ItemStack;

/**
 * Created by Matt on 4/10/2016.
 */
public interface IItemStackTransformer {
    ItemStack transform(ItemStack input);
}
