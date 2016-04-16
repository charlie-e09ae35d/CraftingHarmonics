package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.crafting.FurnaceRecipes;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;

/**
 * Created by Matt on 4/5/2016.
 */
@RecipeOperation(name = "addFurnace")
public class AddFurnaceOperation extends BaseAddOperation {
    private RecipeComponent with;
    private float experience;

    @Override
    public void Init() throws ItemMissingException {
        super.Init();

        if(with.getItemStack() == null) throw new RuntimeException("Unable to find requested input item " + with.toString());
        if(with.hasNbt()) {
            throw new RuntimeException("NBT matching is not supported for furnace recipes.");
        }
    }

    @Override
    public void Apply() {
        System.out.println("Adding furnace recipe for " + output.toString());
        float curXp = FurnaceRecipes.instance().getSmeltingExperience(output.getItemStack());
        if(curXp != 0.0F && curXp != experience) {
            System.out.println(output.toString()
                    + " is already registered as a furnace output. Due to how Minecraft handles smelting XP, this will"
                    + " always give you '" + curXp + "' XP per item instead of the '" + experience + "' you set.");
        }

        FurnaceRecipes.instance().addSmeltingRecipe(with.getItemStack(), output.getItemStack(), experience);
    }

    @Override
    public void Undo() {

    }
}
