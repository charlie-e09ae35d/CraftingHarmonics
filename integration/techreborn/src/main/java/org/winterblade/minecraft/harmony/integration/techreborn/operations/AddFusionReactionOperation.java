package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.addFusionReaction", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddFusionReactionOperation extends BasicOperation {
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
    public void init() throws OperationException {
        if(product == null) throw new OperationException("TechReborn fusion reaction product not found.");
        if(top == null) throw new OperationException("TechReborn fusion reaction top item not found.");
        if(bottom == null) throw new OperationException("TechReborn fusion reaction bottom item not found.");

        recipe = new FusionReactorRecipe(top, bottom, product, euToStart, euPerTick, ticks);
    }

    @Override
    public void apply() {
        FusionReactorRecipeHelper.registerRecipe(recipe);
    }

    @Override
    public void undo() {
        FusionReactorRecipeHelper.reactorRecipes.remove(recipe);
    }
}
