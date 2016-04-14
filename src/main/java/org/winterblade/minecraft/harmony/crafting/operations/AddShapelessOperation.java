package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.crafting.CraftingManager;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.crafting.recipes.ShapelessComponentRecipe;

/**
 * Created by Matt on 4/5/2016.
 */
@RecipeOperation(name = "addShapeless")
public class AddShapelessOperation extends BaseAddOperation {
    /**
     * Serialized properties:
     */
    private RecipeInput[] with;

    @Override
    public void Init() throws ItemMissingException {
        super.Init();

        if(with.length <= 0) throw new ItemMissingException("Shaped recipe has no inputs.");
    }

    @Override
    public void Apply() {
        System.out.println("Adding shapeless recipe for " + output.toString());
        CraftingManager.getInstance().addRecipe(new ShapelessComponentRecipe(output, with));
    }
}
