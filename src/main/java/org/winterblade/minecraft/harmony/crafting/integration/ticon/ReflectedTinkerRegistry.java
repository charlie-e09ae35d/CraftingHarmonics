package org.winterblade.minecraft.harmony.crafting.integration.ticon;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.winterblade.minecraft.harmony.utility.LogHelper;
import slimeknights.mantle.util.RecipeMatchRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.HarvestLevels;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 4/21/2016.
 */
public class ReflectedTinkerRegistry {
    // Registry
    private static List<AlloyRecipe> alloyRegistry;
    private static Map<FluidStack, Integer> smelteryFuels;
    private static Map<String, Material> materials;

    // Harvest levels:
    public static Map<Integer, String> harvestLevelNames;

    // Various other reflection:
    private static Field recipeMatchItems;

    static {
        // Hook directly into this because we can't remove an alloy after it's been added:
        alloyRegistry = ObfuscationReflectionHelper.getPrivateValue(TinkerRegistry.class, null, "alloyRegistry");
        smelteryFuels = ObfuscationReflectionHelper.getPrivateValue(TinkerRegistry.class, null, "smelteryFuels");
        materials = ObfuscationReflectionHelper.getPrivateValue(TinkerRegistry.class, null, "materials");

        // Harvest levels:
        harvestLevelNames = ObfuscationReflectionHelper.getPrivateValue(HarvestLevels.class, null, "harvestLevelNames");
        try {
            recipeMatchItems = RecipeMatchRegistry.class.getDeclaredField("items");
            recipeMatchItems.setAccessible(true);
        } catch (NoSuchFieldException e) {
            LogHelper.warn("Unable to access some of TiCon's Material fields");
        }
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

        try {
            // I imagine there's probably a faster way to check this, but,
            // we can't use Sided Proxies, as that just crashes the game.
            if(Material.class.getDeclaredField("renderInfo") != null) {
                output.setRenderInfo(material.renderInfo);
            }
        } catch (NoSuchFieldException e) {
            // We're on the server.
        }

        material.getDefaultTraits().forEach(output::addTrait);

        for(IMaterialStats stats : material.getAllStats()) {
            output.addStats(stats);
            for(ITrait trait : output.getAllTraitsForStats(stats.getIdentifier())) {
                output.addTrait(trait, stats.getIdentifier());
            }
        }

        if(recipeMatchItems != null) {
            try {
                recipeMatchItems.set(output, recipeMatchItems.get(material));
            } catch (IllegalAccessException e) {
                LogHelper.warn("Unable to copy TiCon material's items.");
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

    /**
     * Gets the harvest level at the given value:
     * @param level    The level to get
     * @return         The harvest level (with color)
     */
    public static String getHarvestLevelName(int level) {
        return harvestLevelNames.get(level);
    }

    /**
     * Sets a harvest level to the given name:
     * @param level    The level to set
     * @param name     The name to set it to
     */
    public static void setHarvestLevelName(int level, String name) {

        harvestLevelNames.put(level, name);
    }

    /**
     * Encodes a color
     * @param color    The color to encode
     * @return         The string representation of the color
     */
    public static String encodeColor(String color) {
        if(color == null || color.equals("") || color.length() != 7) return encodeColor(0xFFFFFF);

        Integer r = Integer.valueOf(color.substring(1, 3), 16);
        Integer g = Integer.valueOf(color.substring(3, 5), 16);
        Integer b = Integer.valueOf(color.substring(5, 7), 16);

        return encodeColor(r, g, b);
    }

    private static String encodeColor(int color) {
        int r = ((color >> 16) & 255);
        int g = ((color >>  8) & 255);
        int b = ((color) & 255);
        return encodeColor(r, g, b);
    }

    private static String encodeColor(int r, int g, int b) {
        int MARKER = 0xE700;
        return String.format("%c%c%c",
                ((char)(MARKER + (r&0xFF))),
                ((char)(MARKER + (g&0xFF))),
                ((char)(MARKER + (b&0xFF))));
    }
}

