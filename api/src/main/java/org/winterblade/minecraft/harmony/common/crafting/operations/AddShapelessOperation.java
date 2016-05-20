package org.winterblade.minecraft.harmony.common.crafting.operations;

import net.minecraft.item.crafting.CraftingManager;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.api.crafting.recipes.ShapelessComponentRecipe;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 4/5/2016.
 */
@Operation(name = "addShapeless")
public class AddShapelessOperation extends BaseAddOperation {
    /**
     * Serialized properties:
     */
    private RecipeInput[] with;
    protected ShapelessComponentRecipe recipe;

    @Override
    public void init() throws OperationException {
        super.init();

        if(with.length <= 0) throw new OperationException("Shaped recipe has no inputs.");
        recipe = new ShapelessComponentRecipe(output.getItemStack(), with);
    }

    @Override
    public void apply() {
        LogHelper.info("Adding shapeless recipe for " + ItemUtility.outputItemName(output.getItemStack()));
        CraftingManager.getInstance().addRecipe(recipe);
    }

    @Override
    public void undo() {
        CraftingManager.getInstance().getRecipeList().remove(recipe);
    }
}
