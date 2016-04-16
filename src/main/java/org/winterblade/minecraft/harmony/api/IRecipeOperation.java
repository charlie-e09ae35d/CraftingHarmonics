package org.winterblade.minecraft.harmony.api;

import org.winterblade.minecraft.harmony.crafting.ItemMissingException;

/**
 * Created by Matt on 4/5/2016.
 */
public interface IRecipeOperation extends Comparable<IRecipeOperation> {
    void Init() throws ItemMissingException;

    void Apply();

    void Undo();
}
