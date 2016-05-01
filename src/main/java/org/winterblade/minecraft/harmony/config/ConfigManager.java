package org.winterblade.minecraft.harmony.config;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.harmony.utility.LogHelper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/5/2016.
 */
public class ConfigManager {

    private final String configPath;
    private final List<File> setFiles = new ArrayList<>();

    /**
     * Generates a new config manager using the config path
     * @param configPath The path to the config directory
     */
    public ConfigManager(String configPath) {
        this.configPath = configPath;
        setupRecipeSets();
    }

    private void setupRecipeSets() {
        LogHelper.info("Reading set definitions from " + configPath + "Sets/");
        setFiles.clear();
        File setsDir = new File(configPath + "Sets/");

        // Make sure we have a set directory before trying to iterate it...
        if(!setsDir.exists()) {
            if(!setsDir.mkdirs()) {
                LogHelper.fatal("Unable to create config/CraftingHarmonics/Sets/ for CraftingHarmonics.");
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
                LogHelper.error("Error writing sample config to config directory.");
                return;
            }
        }

        // Also make sure it's actually a directory
        if(!setsDir.isDirectory()) {
            LogHelper.error("config/CraftingHarmonics/Sets exists, but isn't a directory; this is unacceptable.");
            return;
        }

        File[] files = setsDir.listFiles();

        // Make sure we read something...
        if(files == null) {
            LogHelper.error("config/CraftingHarmonics/Sets existed, but then it didn't, and we couldn't get files from it.");
            return;
        }

        for(File config : files) {
            if(!config.getName().endsWith(".json")) continue;
            setFiles.add(config);
        }
    }

    public void processSetFiles() {
        if(setFiles.size() <= 0) return;

        // > Be fancy.
        ProgressBar setProgress = ProgressManager.push("Processing", setFiles.size());

        for(File config : setFiles) {
            setProgress.step(config.getName());
            LogHelper.info("Reading set definition " + config.getPath());
            try {
                NashornConfigProcessor.getInstance().ReadConfigFile(config);
            } catch (Exception e) {
                LogHelper.error("Error processing Set file " + config.getPath() + ": " + e.getMessage());
            }
        }

        // > Stop being fancy
        ProgressManager.pop(setProgress);
    }

    /**
     * Reloads the config set
     */
    public void reload() {
        setupRecipeSets();
        NashornConfigProcessor.getInstance().reloadConfigs();
        processSetFiles();
    }
}
