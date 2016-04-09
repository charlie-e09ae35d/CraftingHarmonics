package org.winterblade.minecraft.harmony.crafting.operations;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
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
    private String[] with;

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

        for(String item : with) {
            if(ItemRegistry.IsOreDictionaryEntry(item)) {
                input.add(ItemRegistry.GetOreDictionaryName(item));
            } else {
                ItemStack inputItem = ItemRegistry.TranslateToItemStack(item);
                if (inputItem == null) continue;
                input.add(inputItem);
                if(!isNbt && inputItem.hasTagCompound()) isNbt = true;
            }
        }
    }

    @Override
    public void Apply() {
        System.out.println("Adding shapeless recipe for " + outputItemStack.getUnlocalizedName());
        CraftingManager.getInstance().addRecipe(
                isNbt
                    ? new ShapelessNbtMatchingRecipe(outputItemStack, input.toArray())
                    : new ShapelessOreRecipe(outputItemStack, input.toArray()));
    }

    /**
     * Used to convert the provided operation from the file into the given recipe.
     *
     * @param data The operation data
     */
    @Override
    protected void ReadData(ScriptObjectMirror data) {
        // TODO
    }
}
