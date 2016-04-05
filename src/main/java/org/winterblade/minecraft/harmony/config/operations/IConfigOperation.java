package org.winterblade.minecraft.harmony.config.operations;

import net.minecraft.item.crafting.IRecipe;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;

/**
 * Created by Matt on 4/5/2016.
 */
public interface IConfigOperation {
    void Init() throws ItemMissingException;
}
