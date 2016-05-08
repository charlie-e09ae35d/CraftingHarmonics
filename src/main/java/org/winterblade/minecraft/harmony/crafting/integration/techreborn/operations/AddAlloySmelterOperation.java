package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import techreborn.api.recipe.machines.AlloySmelterRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addAlloySmelter", dependsOn = "techreborn")
public class AddAlloySmelterOperation extends BaseTechRebornAddOperation {
    @Override
    public void Init() throws ItemMissingException {
        if(what.length != 1) throw new ItemMissingException("Alloy smelter recipes require 1 output item.");
        if(with.length != 2) throw new ItemMissingException("Alloy smelter recipes require 2 input items.");
        recipe = new AlloySmelterRecipe(with[0], with[1], what[0], ticks, euPerTick);
    }
}
