package org.winterblade.minecraft.harmony.integration.bloodmagic.operations;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.integration.bloodmagic.ReflectedBloodMagicRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 4/22/2016.
 */
@Operation(name = "addHellfireForgeRecipe", dependsOn = "BloodMagic")
public class AddHellfireForgeRecipe extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack output;
    private double minSouls;
    private double drain;
    private RecipeInput[] with;

    /*
     * Computed properties
     */
    private transient TartaricForgeRecipe recipe;

    @Override
    public void init() throws OperationException {
        recipe = new TartaricForgeRecipe(output, minSouls, drain, RecipeInput.getFacsimileItems(with));
    }

    @Override
    public void apply() {
        LogHelper.info("Adding Hellfire Forge recipe for '" + ItemUtility.outputItemName(output) + "'.");
        ReflectedBloodMagicRegistry.addHellfireForgeRecipe(recipe);
    }

    @Override
    public void undo() {
        ReflectedBloodMagicRegistry.removeHellfireForgeRecipe(recipe);
    }
}
