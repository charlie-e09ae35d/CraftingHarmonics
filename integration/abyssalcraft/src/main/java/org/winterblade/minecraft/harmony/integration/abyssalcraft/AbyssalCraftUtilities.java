package org.winterblade.minecraft.harmony.integration.abyssalcraft;

import com.google.common.collect.Lists;
import com.shinoow.abyssalcraft.api.AbyssalCraftAPI;
import com.shinoow.abyssalcraft.api.recipe.CrystallizerRecipes;
import com.shinoow.abyssalcraft.api.recipe.Materialization;
import com.shinoow.abyssalcraft.api.recipe.MaterializerRecipes;
import com.shinoow.abyssalcraft.api.recipe.TransmutatorRecipes;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 5/28/2016.
 */
public class AbyssalCraftUtilities {
    public static List<Block> shoggothBlockBlacklist = Lists.newArrayList();
    public static List<ItemStack> crystals = Lists.newArrayList();

    // Crystallizer
    public static Map<ItemStack, ItemStack[]> crystallizationList = new HashMap();
    public static Map<ItemStack, Float> crystallizerXpList = new HashMap();

    // Materialization
    public static final List<Materialization> materializationList;

    // Transmutator
    public static final Map<ItemStack, ItemStack> transmutationList;
    public static Map<ItemStack, Float> transmutationXpList = new HashMap();

    static {
        // Do the usual breaking and entering thing...
        crystallizerXpList = ObfuscationReflectionHelper
                .getPrivateValue(CrystallizerRecipes.class, CrystallizerRecipes.instance(), "experienceList");
        transmutationXpList = ObfuscationReflectionHelper
                .getPrivateValue(TransmutatorRecipes.class, TransmutatorRecipes.instance(), "experienceList");


        // Also get copies of the things that aren't as well protected...
        crystallizationList = CrystallizerRecipes.instance().getCrystallizationList();
        materializationList = MaterializerRecipes.instance().getMaterializationList();
        transmutationList = TransmutatorRecipes.instance().getTransmutationList();
        shoggothBlockBlacklist = AbyssalCraftAPI.getShoggothBlockBlacklist();
        crystals = AbyssalCraftAPI.getCrystals();

        // TODO: Fuel handling
    }
}