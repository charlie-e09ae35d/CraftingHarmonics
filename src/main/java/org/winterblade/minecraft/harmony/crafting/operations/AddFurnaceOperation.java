package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.components.RecipeComponent;
import org.winterblade.minecraft.harmony.common.crafting.operations.BaseAddOperation;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.Map;

/**
 * Created by Matt on 4/5/2016.
 */
@Operation(name = "addFurnace")
public class AddFurnaceOperation extends BaseAddOperation {
    private static Map<ItemStack, Float> experienceList;

    private RecipeComponent with;
    private float experience;
    private ItemStack inputStack;
    private ItemStack outputStack;
    private boolean setXp = false;

    static {
        experienceList = ObfuscationReflectionHelper.getPrivateValue(FurnaceRecipes.class, FurnaceRecipes.instance(), 2);
    }

    @Override
    public void init() throws OperationException {
        super.init();

        if(with == null) throw new OperationException("You must specify 'with' to add things to the furnace.");
        if(with.getItemStack() == null) throw new OperationException("Unable to find requested input item " + with.toString());
        if(with.hasNbt()) {
            throw new OperationException("NBT matching is not supported for furnace recipes.");
        }
        inputStack = with.getItemStack();
        outputStack = output.getItemStack();
    }

    @Override
    public void apply() {
        LogHelper.info("Adding furnace recipe for " + output.toString());
        float curXp = FurnaceRecipes.instance().getSmeltingExperience(output.getItemStack());
        setXp = curXp == 0.0F;
        if(curXp != 0.0F && curXp != experience) {
            LogHelper.warn(output.toString()
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
    public void undo() {
        FurnaceRecipes.instance().getSmeltingList().remove(inputStack);
        if(setXp) experienceList.remove(outputStack);
    }
}
