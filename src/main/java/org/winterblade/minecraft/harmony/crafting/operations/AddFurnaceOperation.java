package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/5/2016.
 */
@RecipeOperation(name = "addFurnace")
public class AddFurnaceOperation extends BaseAddOperation {
    /**
     * Serialized properties:
     */
    private String with;
    private float experience;

    /**
     * Actual items and whatnot
     */
    private transient ItemStack inputItem;
    private transient ItemStack outputItemStack;

    @Override
    public void Init() throws ItemMissingException {
        outputItemStack = ItemRegistry.TranslateToItemStack(output, quantity);
        if(outputItemStack == null) throw new RuntimeException("Unable to find requested output item '" + output + "'.");

        inputItem = ItemRegistry.TranslateToItemStack(with);
        if(inputItem == null) throw new RuntimeException("Unable to find requested input item '" + with + "'.");
    }

    @Override
    public void Apply() {
        System.out.println("Adding furnace recipe for " + outputItemStack.getUnlocalizedName());
        float curXp = FurnaceRecipes.instance().getSmeltingExperience(outputItemStack);
        if(curXp != 0.0F && curXp != experience) {
            System.out.println(outputItemStack.getUnlocalizedName()
                    + " is already registered as a furnace output. Due to how Minecraft handles smelting XP, this will"
                    + " always give you '" + curXp + "' XP per item instead of the '" + experience + "' you set.");
        }
        FurnaceRecipes.instance().addSmeltingRecipe(inputItem, outputItemStack, experience);
    }

}
