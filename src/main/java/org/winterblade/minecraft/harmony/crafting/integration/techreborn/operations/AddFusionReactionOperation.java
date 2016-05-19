package org.winterblade.minecraft.harmony.crafting.integration.techreborn.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.techreborn.RebornRecipeUtils;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;

/**
 * Created by Matt on 5/8/2016.
 */
@RecipeOperation(name = "TechReborn.addFusionReaction", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddFusionReactionOperation extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private ItemStack product;
    private ItemStack top;
    private ItemStack bottom;

    private double euToStart;
    private double euPerTick;
    private int ticks;

    /*
     * Computed properties
     */
    private transient FusionReactorRecipe recipe;

    @Override
    public void Init() throws ItemMissingException {
        if(product == null) throw new ItemMissingException("TechReborn fusion reaction product not found.");
        if(top == null) throw new ItemMissingException("TechReborn fusion reaction top item not found.");
        if(bottom == null) throw new ItemMissingException("TechReborn fusion reaction bottom item not found.");

        recipe = new FusionReactorRecipe(top, bottom, product, euToStart, euPerTick, ticks);
    }

    @Override
    public void Apply() {
        FusionReactorRecipeHelper.registerRecipe(recipe);
    }

    @Override
    public void Undo() {
        FusionReactorRecipeHelper.reactorRecipes.remove(recipe);
    }
}
