package org.winterblade.minecraft.harmony.crafting.integration.ticon;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 4/21/2016.
 */
public class ReflectedTinkerRegistry {
    private static List<AlloyRecipe> alloyRegistry;
    private static Map<FluidStack, Integer> smelteryFuels;

    static {
        // Hook directly into this because we can't remove an alloy after it's been added:
        alloyRegistry = ObfuscationReflectionHelper.getPrivateValue(TinkerRegistry.class, null, "alloyRegistry");
        smelteryFuels = ObfuscationReflectionHelper.getPrivateValue(TinkerRegistry.class, null, "smelteryFuels");
    }

    private ReflectedTinkerRegistry() {}

    public static void addAlloy(AlloyRecipe recipe) {
        alloyRegistry.add(recipe);
    }

    public static void removeAlloy(AlloyRecipe recipe) {
        alloyRegistry.remove(recipe);
    }

    public static void addFuel(FluidStack what, int duration) {
        TinkerRegistry.registerSmelteryFuel(what, duration);
    }

    /**
     * Remove a smeltery fuel and return the current stack and duration
     * @param what    The fuel to remove
     * @return        The stack itself and the duration; used to re-register it.
     */
    public static Map.Entry<FluidStack, Integer> removeFuel(FluidStack what) {
        for(Iterator<Map.Entry<FluidStack, Integer>> it = smelteryFuels.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<FluidStack, Integer> entry = it.next();

            if(!entry.getKey().isFluidEqual(what)) continue;

            it.remove();
            return entry;
        }

        return null;
    }

    /**
     * Add a recipe to the casting table
     * @param recipe    The recipe to add
     */
    public static void addTableCast(CastingRecipe recipe) {
        TinkerRegistry.registerTableCasting(recipe);
    }

    /**
     * Remove a recipe from the casting table
     * @param recipe    The recipe to remove
     */
    public static void removeTableCast(CastingRecipe recipe) {
        TinkerRegistry.getAllTableCastingRecipes().remove(recipe);
    }
}
