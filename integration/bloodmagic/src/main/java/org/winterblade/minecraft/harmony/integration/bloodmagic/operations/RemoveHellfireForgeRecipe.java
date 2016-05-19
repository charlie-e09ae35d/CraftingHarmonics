package org.winterblade.minecraft.harmony.integration.bloodmagic.operations;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.integration.bloodmagic.ReflectedBloodMagicRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.List;

/**
 * Created by Matt on 4/21/2016.
 */
@RecipeOperation(name = "removeHellfireForgeRecipe", dependsOn = "BloodMagic")
public class RemoveHellfireForgeRecipe extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;
    private ItemStack[] with;

    /*
     * Computed properties
     */
    private transient TartaricForgeRecipe recipe;

    @Override
    public void Init() throws ItemMissingException {
        List<TartaricForgeRecipe> recipes = TartaricForgeRecipeRegistry.getRecipeList();

        for(TartaricForgeRecipe r : recipes) {
            if(!matches(r)) continue;

            recipe = r;
            break;
        }
    }

    @Override
    public void Apply() {
        if(recipe == null) return;
        LogHelper.info("Removing Hellfire Forge recipe for '" + what.toString() + "'.");
        ReflectedBloodMagicRegistry.removeHellfireForgeRecipe(recipe);
    }

    @Override
    public void Undo() {
        if(recipe == null) return;
        ReflectedBloodMagicRegistry.addHellfireForgeRecipe(recipe);
    }

    /**
     * Check if the given recipe matches
     * @param r    The recipe to check
     * @return     True if it matches
     */
    private boolean matches(TartaricForgeRecipe r) {
        if(!r.getRecipeOutput().isItemEqualIgnoreDurability(what)) return false;

        // If that's all we need to check:
        if(with == null || with.length <= 0) return true;

        // If our lengths are different, this obviously isn't it
        if(with.length != r.getInput().size()) return false;

        // Go through our list and figure out if everything's the same:
        // (this should always be 1 item in the current version)
        for(ItemStack input : with) {
            boolean matched = false;

            for(Object recipeInput : r.getInput()) {
                // Deal with the fact this can be either an ItemStack or a List<ItemStack>.
                if(recipeInput instanceof ItemStack) {
                    if(!((ItemStack) recipeInput).isItemEqualIgnoreDurability(input)) continue;
                } else if(recipeInput instanceof List) {
                    if(!OreDictionary.containsMatch(false, (List)recipeInput, input)) continue;
                }

                matched = true;
                break;
            }

            if(!matched) return false;
        }

        return true;
    }
}
