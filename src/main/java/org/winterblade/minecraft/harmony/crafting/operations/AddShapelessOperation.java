package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;
import org.winterblade.minecraft.harmony.crafting.recipes.ShapelessNbtMatchingRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/5/2016.
 */
@RecipeOperation(name = "addShapeless")
public class AddShapelessOperation extends BaseAddOperation {
    /**
     * Serialized properties:
     */
    private RecipeComponent[] with;

    /**
     * Actual items and whatnot
     */
    private transient List<Object> input;
    private transient boolean isNbt;

    @Override
    public void Init() throws ItemMissingException {
        super.Init();

        if(with.length <= 0) throw new ItemMissingException("Shaped recipe has no inputs.");

        input = new ArrayList<>();

        for(RecipeComponent item : with) {
            if(item.isOreDict()) {
                input.add(item.getOreDictName());
            } else {
                ItemStack inputItem = item.getItemStack();
                if (inputItem == null) continue;
                input.add(inputItem);
                if(!isNbt && inputItem.hasTagCompound()) isNbt = true;
            }
        }
    }

    @Override
    public void Apply() {
        System.out.println("Adding shapeless recipe for " + output.getItemStack().getUnlocalizedName());
        CraftingManager.getInstance().addRecipe(
                isNbt
                    ? new ShapelessNbtMatchingRecipe(output.getItemStack(), input.toArray())
                    : new ShapelessOreRecipe(output.getItemStack(), input.toArray()));
    }
}
