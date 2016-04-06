package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.FuelRegistry;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/6/2016.
 */
@RecipeOperation(name = "addFurnaceFuel")
public class AddFurnaceFuel extends BaseRecipeOperation {
    /**
     * Serialized properties
     */
    private String what;
    private int burnTime;

    /**
     * Computed properties
     */
    private transient ItemStack fuel;

    @Override
    public void Init() throws ItemMissingException {
        fuel = ItemRegistry.TranslateToItemStack(what);
        if (fuel == null) throw new RuntimeException("Unable to find requested fuel item '" + what + "'.");
    }

    @Override
    public void Apply() {
        System.out.println("Registering fuel '" + fuel.getUnlocalizedName() + "' with burn time '" + burnTime + "'.");
        int curBurnTime = GameRegistry.getFuelValue(fuel);
        if (curBurnTime > burnTime) {
            System.out.println("Currently '" + fuel.getUnlocalizedName() + "' is registered at a higher burn time " +
                    "than you've requested; we can't override this at the moment.");
            return;
        }

        FuelRegistry.getInstance().AddFuel(fuel, burnTime);
    }
}
