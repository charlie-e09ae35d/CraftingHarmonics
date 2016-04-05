package org.winterblade.minecraft.harmony.config;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.ArrayUtils;
import org.winterblade.minecraft.harmony.config.operations.ConfigOperation;
import org.winterblade.minecraft.harmony.config.operations.CraftingSet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 4/5/2016.
 */
public class ConfigManager {

    private final String configPath;

    private boolean doRegen;
    private Map<String, CraftingSet> sets;

    /**
     * Generates a new config manager using the config path
     * @param configPath The path to the config directory
     */
    public ConfigManager(String configPath) {
        this.configPath = configPath;
        initBaseSettings();
        setupRecipeSets();
    }

    public CraftingSet GetSet(String set) {
        return sets.get(set);
    }

    private void initBaseSettings() {
        System.out.println("Loading main settings file " + configPath + "Settings.cfg");
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
        System.out.println("Reading set definitions from " + configPath + "Sets/");
        File setsDir = new File(configPath + "Sets/");

        // Make sure we have a set directory before trying to iterate it...
        if(!setsDir.exists()) {
            if(!setsDir.mkdir()) {
                System.err.println("Unable to create config/CraftingHarmonics/Sets/ for CraftingHarmonics.");
                return;
            }

            // Generate a sample config for users...
            // TODO: Fix edge case where this isn't a directory at this point?
            try {
                PrintWriter out = new PrintWriter(setsDir + "/default.json.sample");
                String sampleText = Resources.toString(Resources.getResource("default.json.sample"), Charsets.UTF_8);
                out.println(sampleText);
                out.close();
            } catch (IOException e) {
                System.err.println("Error writing sample config to config directory.");
                return;
            }
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

        List<ConfigFile> setConfigs = new ArrayList<ConfigFile>();

        for(File config : files) {
            if(!config.getName().endsWith(".json")) continue;

            System.out.println("Reading set definition " + config.getPath());
            try {
                // There's a lot that could go wrong here...
                ConfigFile file = ConfigFile.Deserialize(new String(Files.readAllBytes(Paths.get(config.getPath()))));

                // Sanity check to make sure we actually read something useful here...
                if(file.sets == null) {
                    System.err.println(config.getPath() + " had no sets node inside it.");
                    continue;
                }

                // Now let's actually deal with it...
                if(file.name == null) file.name = config.getName();
                setConfigs.add(file);
            } catch (Exception e) {
                System.err.println("Error processing Set file " + config.getPath());
            }
        }

        // Now that we have the files... register them...
        Map<String, ConfigOperation[]> configSets = new HashMap<String, ConfigOperation[]>();

        for(ConfigFile file : setConfigs) {
            System.out.println("Registering sets from '" + file.name + "' - " + file.description);

            for (ConfigSet set : file.sets) {
                if(set.name == null || set.operations == null || set.operations.length <= 0) {
                    System.err.println("Error reading set definition.");
                    continue;
                }

                // Check if we need to merge a list, or if it's a separate set.
                // This may end up complicating things in the long run as it will depend on load order, but
                // will still allow users to modify sets across files as long as they don't step on each other.
                if(configSets.containsKey(set.name)) {
                    configSets.put(set.name, ArrayUtils.addAll(configSets.get(set.name), set.operations));
                } else {
                    configSets.put(set.name, set.operations);
                }
            }
        }

        sets = new HashMap<String, CraftingSet>();

        // Now, process the sets:
        for(String set : configSets.keySet()) {
            sets.put(set, new CraftingSet(configSets.get(set)));
        }
    }
}
