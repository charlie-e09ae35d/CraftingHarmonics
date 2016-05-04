package org.winterblade.minecraft.harmony.crafting.integration.ticon;

import com.google.common.collect.Maps;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.traits.ITrait;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 4/21/2016.
 */
public class ReflectedTinkerRegistry {
    private static List<AlloyRecipe> alloyRegistry;
    private static Map<FluidStack, Integer> smelteryFuels;
    private static Map<String, Material> materials;

    static {
        // Hook directly into this because we can't remove an alloy after it's been added:
        alloyRegistry = ObfuscationReflectionHelper.getPrivateValue(TinkerRegistry.class, null, "alloyRegistry");
        smelteryFuels = ObfuscationReflectionHelper.getPrivateValue(TinkerRegistry.class, null, "smelteryFuels");
        materials = ObfuscationReflectionHelper.getPrivateValue(TinkerRegistry.class, null, "materials");
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

    /**
     * Add a recipe to the casting basin
     * @param recipe    The recipe to add
     */
    public static void addBasinCast(CastingRecipe recipe) {
        TinkerRegistry.registerBasinCasting(recipe);
    }

    /**
     * Remove a recipe from the casting basin
     * @param recipe    The recipe to remove
     */
    public static void removeBasinCast(CastingRecipe recipe) {
        TinkerRegistry.getAllBasinCastingRecipes().remove(recipe);
    }

    /**
     * Gets the given material from the registry, or null if it's unknown
     * @param identifier    The identifier to get
     * @return              The material, or null if it doesn't exist
     */
    public static Material getMaterial(String identifier) {
        Material material = TinkerRegistry.getMaterial(identifier);
        if(material == Material.UNKNOWN) return null;

        return material;
    }

    /**
     * Creates a copy of the given material
     * @param material      The material to copy
     * @return              The copied material
     */
    public static Material copyMaterial(Material material) {
        Material output = new Material(material.identifier, material.materialTextColor);
        output.setFluid(material.getFluid());
        output.setCraftable(material.isCraftable());
        output.setCastable(material.isCastable());
        output.setRenderInfo(material.renderInfo);

        material.getDefaultTraits().forEach(output::addTrait);

        for(IMaterialStats stats : material.getAllStats()) {
            output.addStats(stats);
            for(ITrait trait : output.getAllTraitsForStats(stats.getIdentifier())) {
                output.addTrait(trait, stats.getIdentifier());
            }
        }

        output.setRepresentativeItem(material.getRepresentativeItem());
        output.setShard(material.getShard());

        return output;
    }

    /**
     * Set a material on the materials list
     * @param material    The material to set
     */
    public static void setMaterial(Material material) {
        materials.put(material.getIdentifier(), material);
    }
}
