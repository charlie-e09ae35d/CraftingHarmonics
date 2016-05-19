package org.winterblade.minecraft.harmony.integration.bloodmagic.operations;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.integration.bloodmagic.ReflectedBloodMagicRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 4/22/2016.
 */
@RecipeOperation(name = "addHellfireForgeRecipe", dependsOn = "BloodMagic")
public class AddHellfireForgeRecipe extends BaseRecipeOperation {
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
    public void Init() throws ItemMissingException {
        recipe = new TartaricForgeRecipe(output, minSouls, drain, RecipeInput.getFacsimileItems(with));
    }

    @Override
    public void Apply() {
        LogHelper.info("Adding Hellfire Forge recipe for '" + output.toString() + "'.");
        ReflectedBloodMagicRegistry.addHellfireForgeRecipe(recipe);
    }

    @Override
    public void Undo() {
        ReflectedBloodMagicRegistry.removeHellfireForgeRecipe(recipe);
    }
}
