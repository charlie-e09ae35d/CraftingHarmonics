package org.winterblade.minecraft.harmony.integration.techreborn.operations;

import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.integration.techreborn.RebornRecipeUtils;
import reborncore.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.machines.AlloySmelterRecipe;

/**
 * Created by Matt on 5/8/2016.
 */
@Operation(name = "TechReborn.addAlloySmelter", dependsOn = RebornRecipeUtils.TechRebornModId)
public class AddAlloySmelterOperation extends BaseTechRebornAddOperation {
    public AddAlloySmelterOperation() {super("Alloy Smelter", 2);}

    @Override
    protected IBaseRecipeType getRecipe() throws OperationException {
        return new AlloySmelterRecipe(with[0], with[1], what[0], ticks, euPerTick);
    }
}
