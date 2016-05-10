package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.crafting.CraftingManager;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.crafting.recipes.ShapelessComponentRecipe;
import org.winterblade.minecraft.harmony.utility.LogHelper;

/**
 * Created by Matt on 4/5/2016.
 */
@RecipeOperation(name = "addShapeless")
public class AddShapelessOperation extends BaseAddOperation {
    /**
     * Serialized properties:
     */
    private RecipeInput[] with;
    protected ShapelessComponentRecipe recipe;

    @Override
    public void Init() throws ItemMissingException {
        super.Init();

        if(with.length <= 0) throw new ItemMissingException("Shaped recipe has no inputs.");
        recipe = new ShapelessComponentRecipe(output.getItemStack(), with);
    }

    @Override
    public void Apply() {
        LogHelper.info("Adding shapeless recipe for " + output.toString());
        CraftingManager.getInstance().addRecipe(recipe);
    }

    @Override
    public void Undo() {
        CraftingManager.getInstance().getRecipeList().remove(recipe);
    }
}
