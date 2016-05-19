package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.AlloySmelterRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addAlloySmelter", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddAlloySmelterOperation extends BaseTechRebornAddOperation {
    public AddAlloySmelterOperation() {super("Alloy Smelter", 2);}

    @Override
    protected IBaseRecipeType getRecipe() throws ItemMissingException {
        return new AlloySmelterRecipe(with[0], with[1], what[0], ticks, euPerTick);
    }
}
