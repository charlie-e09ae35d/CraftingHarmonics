package org.winterblade.minecraft.harmony.scripting;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import org.apache.logging.log4j.Logger;
import org.winterblade.minecraft.harmony.api.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.INashornMod;
import org.winterblade.minecraft.scripting.api.IScriptContext;
import org.winterblade.minecraft.scripting.api.NashornMod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 4/8/2016.
 */
@NashornMod
public class NashornConfigProcessor implements INashornMod {
    private final static NashornConfigProcessor instance = new NashornConfigProcessor();

    private final String header;
    public IScriptContext nashorn;
    private final Map<String, String> cache = new HashMap<>();

    public NashornConfigProcessor() {
        // Assign out our script header file...
        String tempHeader;
        try {
            tempHeader = Resources.toString(Resources.getResource("scripts/InternalFileProcessor.js"), Charsets.UTF_8);
        } catch (IOException e) {
            LogHelper.fatal("Unable to load file processing header; things will go badly from here out...");
            tempHeader = "";
        }

        header = tempHeader;
    }

    /**
     * Gets the instance of this singleton
     * @return  The instance of this singleton
     */
    public static NashornConfigProcessor getInstance() {
        return instance;
    }

    public void init(IScriptContext nashorn) {
        this.nashorn = nashorn;

        // Actually try and load our script header into Nashorn.
        nashorn.eval(header);
    }


    public void ReadConfigFile(File file) {
        if(nashorn == null) {
            LogHelper.fatal("Nashorn library isn't loaded; please make sure you have NashornLib in your mods folder.");
        }

        String fileContent = getJsonFileContent(file);

        try {
            processConfig(fileContent);

            // Only put valid configs in the cache:
            cache.put(file.getName(), fileContent);
        } catch (Exception e) {
            LogHelper.error("Error processing Set file " + file.getPath(), e);
        }
    }

    public void processConfig(String config) throws Exception {
        if (config == null) return;
        nashorn.eval(config);
    }

    /**
     * Gets the config cache
     * @return  The config cache
     */
    public ImmutableMap<String,String> getCache() {
        return ImmutableMap.copyOf(cache);
    }

    /**
     * Read the file content into something that's understandable by the system.
     * @param file  The file to process
     * @return      The script.
     */
    private String getJsonFileContent(File file) {
        // Wrap and retreive the content...
        String fileContent = "module.exports = ";
        try {
            fileContent += new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (Exception e) {
            LogHelper.error("Error processing Set file " + file.getPath(),e);
            return null;
        }
        fileContent += "; __CraftingHarmonicsInternal.FileProcessor('" + file.getName() + "',module.exports);";
        return fileContent;
    }

    @Override
    public Logger getLogger() {
        return LogHelper.getLogger();
    }

    @Override
    public String[] getAllowedPackageRoots() {
        return new String[] { "org.winterblade.minecraft.harmony"};
    }

    @Override
    public void onScriptContextCreated(IScriptContext iScriptContext) {
        NashornConfigProcessor.getInstance().init(iScriptContext);
    }

    /**
     * Reloads the configuration files
     */
    public void reloadConfigs() {
        cache.clear();
    }
}
