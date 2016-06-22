package org.winterblade.minecraft.harmony.config;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.utility.ResourceHelper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Matt on 4/5/2016.
 */
public class ConfigManager {
    private final String configPath;
    private final List<File> setFiles = new ArrayList<>();
    private boolean debugMobDropEvents;
    private boolean debugBlockDropEvents;
    private int shedSeconds;
    private int dayTickLength;
    private int potionEffectTicks;
    private int eventTicks;
    private File setsDir;

    /**
     * Generates a new config manager using the config path
     * @param configPath The path to the config directory
     */
    public ConfigManager(String configPath) {
        this.configPath = configPath;
        initBaseSettings();
        setupRecipeSets();
    }

    private void initBaseSettings() {
        Configuration configMain = new Configuration(new File(configPath + "Settings.cfg"));

        configMain.load();

        debugMobDropEvents = configMain.getBoolean("LogMobDropEvents",
                Configuration.CATEGORY_GENERAL,
                false,
                "Should we log entities/damageTypes for mob drops; this will be pretty spammy.");

        debugBlockDropEvents = configMain.getBoolean("LogBlockDropEvents",
                Configuration.CATEGORY_GENERAL,
                false,
                "Should we log blocks/states for block drops; this will be pretty spammy.");

        shedSeconds = configMain.getInt("SecondsBetweenSheds",Configuration.CATEGORY_GENERAL,
                10, 1, 100000, "The number of seconds between calculating if a mob should shed something (if sheds are configured).") * 20;

        potionEffectTicks = configMain.getInt("TicksBetweenPotionEffects",Configuration.CATEGORY_GENERAL,
                5, 1, 2000000, "The number of game ticks between calculating if a mob should get a potion effect (if potion effects are configured).");

        eventTicks = configMain.getInt("TicksBetweenEntityEvents",Configuration.CATEGORY_GENERAL,
                5, 1, 2000000, "The number of game ticks between running entity events (if entity events are configured).");

        // Just in case another mod is modifying this:
        dayTickLength = configMain.getInt("DayTickLength", Configuration.CATEGORY_GENERAL,
                24000, 1, Integer.MAX_VALUE, "The length of a standard Minecraft day, for use in time calculations.");

        configMain.save();
    }


    private void setupRecipeSets() {
        LogHelper.info("Reading set definitions from " + configPath + "Sets/");
        setFiles.clear();
        setsDir = new File(configPath + "Sets/");

        // Make sure we have a set directory before trying to iterate it...
        if(!setsDir.exists()) {
            if(!setsDir.mkdirs()) {
                LogHelper.fatal("Unable to create config/CraftingHarmonics/Sets/ for CraftingHarmonics.");
                return;
            }

            outputSamples();
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
            if(!config.getName().endsWith(".json") && !config.getName().endsWith(".js")) continue;
            setFiles.add(config);
        }
    }

    /**
     * Outputs the sample files into the config directory.
     */
    public void outputSamples() {
        try {
            Set<File> samples = ResourceHelper.getResources(Resources.getResource("samples"));

            // Generate sample configs for users...
            for (File sample : samples) {
                try {
                    PrintWriter out = new PrintWriter(setsDir + "/" + sample.getName());
                    String sampleText = ResourceHelper.getFileContent(sample);
                    out.println(sampleText);
                    out.close();
                } catch (IOException e) {
                    LogHelper.error("Error writing sample config '{}' to config directory.", sample.getName());
                }
            }
        } catch (URISyntaxException e) {
            LogHelper.error("Error getting sample configs.");
        }
    }

    public void processSetFiles() {
        if(setFiles.size() <= 0) return;

        // > Be fancy.
        ProgressBar setProgress = ProgressManager.push("Processing", setFiles.size());

        for(File config : setFiles) {
            setProgress.step(config.getName());
            LogHelper.info("Reading " + config.getPath());
            try {
                NashornConfigProcessor.getInstance().ReadConfigFile(config);
            } catch (Exception e) {
                LogHelper.error("Error processing file " + config.getPath() + ": " + e.getMessage());
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

    public boolean debugMobDropEvents() {
        return debugMobDropEvents;
    }

    public int getShedSeconds() {
        return shedSeconds;
    }

    public int getPotionEffectTicks() {
        return potionEffectTicks;
    }

    public boolean debugBlockDropEvents() {
        return debugBlockDropEvents;
    }

    public int getDayTickLength() {
        return dayTickLength;
    }

    public int getEventTicks() {
        return eventTicks;
    }
}
