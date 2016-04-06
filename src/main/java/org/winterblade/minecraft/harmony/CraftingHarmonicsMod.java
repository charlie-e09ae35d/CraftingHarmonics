package org.winterblade.minecraft.harmony;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.winterblade.minecraft.harmony.config.ConfigManager;
import org.winterblade.minecraft.harmony.config.ConfigOperationDeserializer;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.utility.AnnotatedInstanceUtil;

/**
 * Created by Matt on 4/5/2016.
 */
@Mod(modid = org.winterblade.minecraft.harmony.CraftingHarmonicsMod.MODID, version = org.winterblade.minecraft.harmony.CraftingHarmonicsMod.VERSION)
public class CraftingHarmonicsMod {
    public static final String MODID = "craftingharmonics";
    public static final String VERSION = "0.5";

    private String configPath;
    private ConfigManager configManager;

    public CraftingHarmonicsMod() {
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Load all recipe operations (thanks mezz, who thanks cpw... so also thanks cpw)
        ConfigOperationDeserializer.CreateDeserializers(AnnotatedInstanceUtil.getRecipeOperations(event.getAsmData()));

        // Handle config
        configManager = new ConfigManager(event.getModConfigurationDirectory() + "/CraftingHarmonics/");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ItemRegistry.Init();

        // Always apply the default set:
        CraftingSet defaultSet = configManager.GetSet("default");

        if(defaultSet != null) {
            defaultSet.Init();
            defaultSet.Apply();
        }
    }
}
