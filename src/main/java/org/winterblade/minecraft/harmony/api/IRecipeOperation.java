package org.winterblade.minecraft.harmony.api;

import net.minecraft.item.crafting.IRecipe;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;

/**
 * Created by Matt on 4/5/2016.
 */
public interface IRecipeOperation {
    void Init() throws ItemMissingException;

    void Apply();
}
