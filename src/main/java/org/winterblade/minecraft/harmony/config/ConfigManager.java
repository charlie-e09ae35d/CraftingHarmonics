package org.winterblade.minecraft.harmony.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Matt on 4/5/2016.
 */
public class ConfigManager {

    private final String configPath;

    private boolean doRegen;

    /**
     * Generates a new config manager using the config path
     * @param configPath The path to the config directory
     */
    public ConfigManager(String configPath) {
        this.configPath = configPath;
        initBaseSettings();
    }

    private void initBaseSettings() {
        Configuration configMain = new Configuration(new File(configPath + "Settings.cfg"));

        configMain.load();

        // TODO: Turn this off once we know they really want to do this.
        // ... or just make it a command.
        doRegen = configMain.getBoolean("RegenerateConfigs",
                Configuration.CATEGORY_GENERAL,
                false,
                "Should we regenerate the config files based on current recipes in the game.  This will completely clear your current configs!")
            &&  configMain.getBoolean("RegenerateConfigsConfirm",
                Configuration.CATEGORY_GENERAL,
                false,
                "Should we really regenerate the config files based on current recipes in the game?  I mean, really?");

        configMain.save();
    }

    private void setupRecipeSets() {
        File setsDir = new File(configPath + "Sets/");

        // Make sure we have a set directory before trying to iterate it...
        if(!setsDir.exists() && !setsDir.mkdir()) {
            System.err.println("Unable to create config/CraftingHarmonics/Sets/ for CraftingHarmonics.");
            return;
        }

        // Also make sure it's actually a directory
        if(!setsDir.isDirectory()) {
            System.err.println("config/CraftingHarmonics/Sets exists, but isn't a directory; this is unacceptable.");
            return;
        }

        File[] files = setsDir.listFiles();

        // Make sure we read something...
        if(files == null) {
            System.err.println("config/CraftingHarmonics/Sets existed, but then it didn't, and we couldn't get files from it.");
            return;
        }

        for(File config : files) {
                List<String> encoded;
            try {
                encoded = Files.readAllLines(Paths.get(config.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
