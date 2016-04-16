package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;

import java.util.Map;

/**
 * Created by Matt on 4/5/2016.
 */
@RecipeOperation(name = "addFurnace")
public class AddFurnaceOperation extends BaseAddOperation {
    private static Map<ItemStack, Float> experienceList;

    private RecipeComponent with;
    private float experience;
    private ItemStack inputStack;
    private ItemStack outputStack;
    private boolean setXp = false;

    static {
        experienceList = ObfuscationReflectionHelper.getPrivateValue(FurnaceRecipes.class, FurnaceRecipes.instance(), "experienceList");
    }

    @Override
    public void Init() throws ItemMissingException {
        super.Init();

        if(with.getItemStack() == null) throw new RuntimeException("Unable to find requested input item " + with.toString());
        if(with.hasNbt()) {
            throw new RuntimeException("NBT matching is not supported for furnace recipes.");
        }
        inputStack = with.getItemStack();
        outputStack = output.getItemStack();
    }

    @Override
    public void Apply() {
        System.out.println("Adding furnace recipe for " + output.toString());
        float curXp = FurnaceRecipes.instance().getSmeltingExperience(output.getItemStack());
        setXp = curXp == 0.0F;
        if(curXp != 0.0F && curXp != experience) {
            System.out.println(output.toString()
                    + " is already registered as a furnace output. Due to how Minecraft handles smelting XP, this will"
                    + " always give you '" + curXp + "' XP per item instead of the '" + experience + "' you set.");
        }

        // Only add in an experience entry if it'll matter:
        if(setXp) {
            FurnaceRecipes.instance().addSmeltingRecipe(inputStack, outputStack, experience);
        } else {
            FurnaceRecipes.instance().getSmeltingList().put(inputStack, outputStack);
        }
    }

    @Override
    public void Undo() {
        FurnaceRecipes.instance().getSmeltingList().remove(inputStack);
        if(setXp) experienceList.remove(outputStack);
    }
}
