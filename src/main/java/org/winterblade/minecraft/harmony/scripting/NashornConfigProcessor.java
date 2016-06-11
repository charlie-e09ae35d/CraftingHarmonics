package org.winterblade.minecraft.harmony.scripting;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.apache.logging.log4j.Logger;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.INashornMod;
import org.winterblade.minecraft.scripting.api.IScriptContext;
import org.winterblade.minecraft.scripting.api.NashornMod;

import javax.script.ScriptException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 4/8/2016.
 */
@NashornMod
public class NashornConfigProcessor implements INashornMod {
    private final static NashornConfigProcessor instance = new NashornConfigProcessor();
    private final static String[] headers = new String[]{
            "libs/lodash/lodash.js",
            "InternalFileProcessor.js",
            "models/Set.js"
    };

    public IScriptContext nashorn;
    private final Map<String, String> cache = new HashMap<>();
    private boolean loadedInterops = false;
    private String[] contextRoots = new String[] {"org.winterblade.minecraft.harmony"};

    public NashornConfigProcessor() {}

    /**
     * Gets the instance of this singleton
     * @return  The instance of this singleton
     */
    public static NashornConfigProcessor getInstance() {
        return instance;
    }

    public void init(IScriptContext nashorn) {
        this.nashorn = nashorn;

        // Assign out our script header files...
        long timeMillis = System.currentTimeMillis();
        for(String headerPath : headers) {
            try {
                nashorn.eval(Resources.toString(Resources.getResource("scripts/" + headerPath), Charsets.UTF_8));
            } catch (Exception e) {
                LogHelper.fatal("Unable to process header scripts/{}; things will go badly from here out...", headerPath);
            }
        }
        LogHelper.info("Loading script headers took {} milliseconds.", System.currentTimeMillis() - timeMillis);
    }


    public void ReadConfigFile(File file) {
        if(nashorn == null) {
            LogHelper.fatal("Nashorn library isn't loaded; please make sure you have NashornLib in your mods folder.");
        }

        String fileContent = file.getName().endsWith(".json") ? getJsonFileContent(file) : getFileContent(file);

        try {
            processConfig(fileContent);

            // Only put valid configs in the cache:
            cache.put(file.getName(), fileContent);
        } catch (Exception e) {
            LogHelper.error("Error processing file " + file.getPath(), e);
        }
    }

    public void processConfig(String config) throws Exception {
        if (!loadedInterops) loadInterops();

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
        return "module.exports = " + getFileContent(file) + "; __CraftingHarmonicsInternal.FileProcessor('"
                + file.getName() + "',module.exports);";
    }

    /**
     * Read the contents of the given file
     * @param file    The file to read
     * @return        The contents, or an empty string if it failed.
     */
    private String getFileContent(File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (Exception e) {
            LogHelper.error("Error processing file " + file.getPath(),e);
            return "";
        }
    }

    /**
     * Attach all interops
     */
    private void loadInterops() {
        loadedInterops = true;

        Map<String, String[]> interops = ScriptInteropRegistry.getInterops();

        // First we need to whitelist them in the security provider...
        List<String> updatedRoots = Lists.newArrayList(contextRoots);
        updatedRoots.addAll(interops.keySet());
        contextRoots = updatedRoots.toArray(new String[updatedRoots.size()]);

        // Then we can actually add them...
        for(Map.Entry<String, String[]> interop : interops.entrySet()) {
            try {
                nashorn.invokeFunction("__CraftingHarmonicsInternalAddInterop", interop.getKey(), interop.getValue());
            } catch (ScriptException | NoSuchMethodException e) {
                LogHelper.error("Unable to add interop class '" + interop.getKey() + "'.", e);
            }
        }
    }

    @Override
    public Logger getLogger() {
        return LogHelper.getLogger();
    }

    @Override
    public String[] getAllowedPackageRoots() {
        return contextRoots;
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
