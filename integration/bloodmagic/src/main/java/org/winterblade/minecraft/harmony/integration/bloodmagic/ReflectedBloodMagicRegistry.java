package org.winterblade.minecraft.harmony.integration.bloodmagic;

import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyCircleRenderer;
import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry.AlchemyArrayRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyTableRecipeRegistry;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import WayofTime.bloodmagic.api.registry.TartaricForgeRecipeRegistry;
import com.google.common.collect.BiMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Matt on 4/21/2016.
 */
public class ReflectedBloodMagicRegistry {
    private static BiMap<List<ItemStack>, AltarRecipeRegistry.AltarRecipe> altarRecipes;
    private static List<TartaricForgeRecipe> tartaricForgeRecipes;
    private static BiMap<List<ItemStack>, AlchemyArrayRecipe> alchemyArrayRecipes;
    private static List<AlchemyTableRecipe> alchemyTableRecipes = new ArrayList<>();

    static {
        // I really wish mod authors would better support removing recipes from their mods...
        altarRecipes = ObfuscationReflectionHelper.getPrivateValue(AltarRecipeRegistry.class, null, "recipes");
        tartaricForgeRecipes = ObfuscationReflectionHelper.getPrivateValue(TartaricForgeRecipeRegistry.class, null, "recipeList");
        alchemyArrayRecipes = ObfuscationReflectionHelper.getPrivateValue(AlchemyArrayRecipeRegistry.class, null, "recipes");
        alchemyTableRecipes = ObfuscationReflectionHelper.getPrivateValue(AlchemyTableRecipeRegistry.class, null, "recipeList");
    }

    private ReflectedBloodMagicRegistry() {}

    public static void addAltarRecipe(AltarRecipeRegistry.AltarRecipe recipe) {
        AltarRecipeRegistry.registerRecipe(recipe);
    }

    public static void removeAltarRecipe(AltarRecipeRegistry.AltarRecipe recipe) {
        altarRecipes.remove(recipe.getInput());
    }

    public static void addHellfireForgeRecipe(TartaricForgeRecipe recipe) {
        TartaricForgeRecipeRegistry.registerRecipe(recipe);
    }

    public static void removeHellfireForgeRecipe(TartaricForgeRecipe recipe) {
        tartaricForgeRecipes.remove(recipe);
    }

    public static void addAlchemyArray(ItemStack input, @Nullable ItemStack catalystStack, AlchemyArrayEffect arrayEffect, ResourceLocation circleRenderer) {
        AlchemyArrayRecipeRegistry.registerRecipe(Collections.singletonList(input), catalystStack, arrayEffect, circleRenderer);
    }

    public static void addAlchemyArray(String input, @Nullable ItemStack catalystStack, AlchemyArrayEffect arrayEffect, ResourceLocation circleRenderer) {
        AlchemyArrayRecipeRegistry.registerRecipe(input, catalystStack, arrayEffect, circleRenderer);
    }

    public static void addAlchemyArray(List<ItemStack> input, @Nullable ItemStack catalyst, AlchemyArrayEffect effect, AlchemyCircleRenderer renderer) {
        AlchemyArrayRecipeRegistry.registerRecipe(input, catalyst, effect, renderer);
    }

    /**
     * Remove an alchemy array with the given item stack / catalyst combo
     * @param inputItem    The ItemStack to check against
     * @param catalyst     The catalyst to check against; can be null
     * @return             The removed recipe; null if none matched.
     */
    public static RemovedAlchemyArray removeAlchemyArray(ItemStack inputItem, @Nullable ItemStack catalyst) {
        return removeAlchemyArray(inputItem == null ? null : Collections.singletonList(inputItem), catalyst);
    }

    /**
     * Remove an alchemy array with the given ore dictionary / catalyst combo
     * @param input        The ore dictionary to check against
     * @param catalyst     The catalyst to check against; can be null
     * @return             The removed recipe; null if none matched.
     */
    public static RemovedAlchemyArray removeAlchemyArray(String input, ItemStack catalyst) {
        return removeAlchemyArray(OreDictionary.getOres(input), catalyst);
    }

    /**
     * Remove an alchemy array with the given ItemStack list / catalyst combo
     * @param input        The ItemStack list to check against
     * @param catalyst     The catalyst to check against; can be null
     * @return             The removed recipe; null if none matched.
     */
    public static RemovedAlchemyArray removeAlchemyArray(List<ItemStack> input, @Nullable ItemStack catalyst) {
        for (Iterator<Entry<List<ItemStack>, AlchemyArrayRecipe>> it = alchemyArrayRecipes.entrySet().iterator(); it.hasNext(); )
        {
            AlchemyArrayRecipe recipe = it.next().getValue();

            // Check if we match...
            if(!alchemyArrayMatches(recipe, input, catalyst)) continue;

            // Find our effect:
            AlchemyArrayEffect effect = null;
            ItemStackWrapper wrapper = null;
            for(Iterator<Entry<ItemStackWrapper, AlchemyArrayEffect>> catIt = recipe.catalystMap.entrySet().iterator(); catIt.hasNext(); ) {
                Entry<ItemStackWrapper, AlchemyArrayEffect> catalystEntry = catIt.next();
                if(!catalystEntry.getKey().toStack().isItemEqualIgnoreDurability(catalyst)) continue;

                // Set our effect, then remove the catalyst:
                effect = catalystEntry.getValue();
                catIt.remove();
                break;
            }

            // Remove it if we only had the one catalyst
            if(recipe.catalystMap.size() <= 0) {
                it.remove();
            }

            // Finally, return our removed recipe
            return new RemovedAlchemyArray(input, catalyst, effect, recipe.circleRenderer);
        }

        return null;
    }

    /**
     * Checks the given recipe against the input/catalyst
     * @param recipe    The recipe to check
     * @param input     The input to check against
     * @param catalyst  The catalyst to check against
     * @return          True if the recipe matches; false otherwise
     */
    private static boolean alchemyArrayMatches(AlchemyArrayRecipe recipe, List<ItemStack> input, @Nullable ItemStack catalyst) {
        return recipe.doesInputMatchRecipe(input)&& recipe.getAlchemyArrayEffectForCatalyst(catalyst) != null;
    }

    /**
     * Adds a recipe directly to the alchemy array
     * @param recipe    The recipe to add
     */
    public static void addAlchemyArray(AlchemyArrayRecipe recipe) {
        alchemyArrayRecipes.put(recipe.getInput(), recipe);
    }

    public static class RemovedAlchemyArray {
        public List<ItemStack> input;
        public ItemStack catalyst;
        public AlchemyArrayEffect effect;
        public AlchemyCircleRenderer renderer;

        public RemovedAlchemyArray(List<ItemStack> input, ItemStack catalyst,
                                   AlchemyArrayEffect effect, AlchemyCircleRenderer renderer) {
            this.input = input;
            this.catalyst = catalyst;
            this.effect = effect;
            this.renderer = renderer;
        }
    }

    /*
     * Alchemy table stuff
     */
    public static void addAlchemyTableRecipe(AlchemyTableRecipe recipe) {
        AlchemyTableRecipeRegistry.registerRecipe(recipe);
    }

    public static void removeAlchemyTableRecipe(AlchemyTableRecipe recipe) {
        alchemyTableRecipes.remove(recipe);
    }

    public static List<AlchemyTableRecipe> findMatchingAlchemyTableRecipes(ItemStack output, @Nullable Object[] inputs) {
        List<AlchemyTableRecipe> matches = new ArrayList<>();

        if(inputs == null) inputs = new Object[0];

        for (AlchemyTableRecipe recipe : alchemyTableRecipes) {
            if (!matchAlchemyTableRecipe(recipe, output, inputs)) continue;
            matches.add(recipe);
        }

        return matches;
    }

    /**
     * Match an alchemy table recipe on the given output/input combo
     * @param recipe    The recipe to match
     * @param output    The output to look for
     * @param inputs    The input array to go through; can be partial/non-existent
     * @return          True if the recipe matches the required components; false otherwise.
     */
    private static boolean matchAlchemyTableRecipe(AlchemyTableRecipe recipe, ItemStack output, Object[] inputs) {
        if(!recipe.getRecipeOutput().isItemEqualIgnoreDurability(output)) return false;

        ArrayList<Object> recipeInputs = recipe.getInput();

        for (Object input : inputs) {
            boolean matched = false;

            for (Object recipeInput : recipeInputs) {
                if (!recipeInputsMatch(input, recipeInput)) continue;
                matched = true;
                break;
            }

            if (!matched) return false;
        }

        return true;
    }

    /**
     * Checks to see if the two sources match
     * @param toCheck    The input to check
     * @param against    The value to check against
     * @return           True if the inputs match, checking ore-dictionaries
     */
    private static boolean recipeInputsMatch(Object toCheck, Object against) {
        // TODO: Abstract this a bit more and then put it in ItemUtility
        if(against == null) return false;

        if(toCheck instanceof ItemStack) {
            // If we're checking an ore-dict list:
            if(against instanceof List<?>) {
                for(Object i : (List<?>)against) {
                    // Just to double check...
                    if(!(i instanceof ItemStack)) return false;
                    if(((ItemStack) i).isItemEqualIgnoreDurability((ItemStack)toCheck)) return true;
                }

                // If we got here without matching, we're not a match.
                return false;
            }

            // If we're checking just a single item
            if(against instanceof ItemStack) {
                return ((ItemStack) against).isItemEqualIgnoreDurability((ItemStack)toCheck);
            }

            // What?
            return false;
        }

        if(toCheck instanceof String) {
            List<ItemStack> ores = OreDictionary.getOres((String) toCheck);

            for(ItemStack sourceItem : ores) {
                if (against instanceof List<?>) {
                    // Find at least one match in the two lists...
                    for(Object i : (List<?>)against) {
                        // Just to double check...
                        if(!(i instanceof ItemStack)) return false;
                        if(((ItemStack) i).isItemEqualIgnoreDurability(sourceItem)) return true;
                    }
                } else if(against instanceof ItemStack) {
                    if(((ItemStack) against).isItemEqualIgnoreDurability(sourceItem)) return true;
                }
            }
            return false;
        }

        return false;
    }
}
