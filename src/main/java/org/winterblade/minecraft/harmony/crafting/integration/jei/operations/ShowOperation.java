package org.winterblade.minecraft.harmony.crafting.integration.jei.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.jei.Jei;

/**
 * Created by Matt on 5/6/2016.
 */
@RecipeOperation(name = "hide", dependsOn = "JEI")
public class ShowOperation extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;

    @Override
    public void Init() throws ItemMissingException {
        // Do nothing.
    }

    @Override
    public void Apply() {
        Jei.show(what);
    }

    @Override
    public void Undo() {
        Jei.hide(what);
    }
}
