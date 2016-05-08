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
public class AddAlloySmelterOperation extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;
    private ItemStack[] with;
    private int ticks;
    private int euPerTick;

    /*
     * Computed properties
     */
    private transient AlloySmelterRecipe recipe;

    @Override
    public void Init() throws ItemMissingException {
        if(with.length != 2) throw new ItemMissingException("Alloy smelter recipes require 2 input items.");
        recipe = new AlloySmelterRecipe(with[0], with[1], what, ticks, euPerTick);
    }

    @Override
    public void Apply() {
        RebornRecipeUtils.addRecipe(recipe);
    }

    @Override
    public void Undo() {
        RebornRecipeUtils.removeRecipe(recipe);
    }
}
