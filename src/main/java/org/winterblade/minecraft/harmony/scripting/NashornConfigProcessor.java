package org.winterblade.minecraft.harmony.scripting;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Logger;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.INashornMod;
import org.winterblade.minecraft.scripting.api.IScriptContext;
import org.winterblade.minecraft.scripting.api.NashornMod;

import javax.annotation.Nonnull;
import javax.script.ScriptException;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Matt on 4/8/2016.
 */
@NashornMod
public class NashornConfigProcessor implements INashornMod {
    private final static NashornConfigProcessor instance = new NashornConfigProcessor();
    private final static String[] headers = new String[]{
            "libs/lodash/lodash.js",
            "InternalFileProcessor.js",
            "models/Operation.js",
            "models",
            "crafting",
            "drops",
            "integration"
    };

    public IScriptContext nashorn;
    private final Map<String, String> cache = new HashMap<>();
    private boolean loadedInterops = false;
    private String[] contextRoots = new String[] {"org.winterblade.minecraft.harmony"};

    private final Map<String, IFilePreprocessor> preprocessorMap = new HashMap<>();

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
        Set<File> headerFiles = new LinkedHashSet<>();

        for(String headerPath : headers) {
            try {
                headerFiles.addAll(getResources(new File(Resources.getResource("scripts/" + headerPath).toURI())));
            } catch (URISyntaxException e) {
                LogHelper.error("Error reading resource file.", e);
            }
        }

        for (File file : headerFiles) {
            try {
                nashorn.eval(Resources.toString(file.toURI().toURL(), Charsets.UTF_8));
            } catch (Exception e) {
                LogHelper.fatal("Unable to process header file {}; things will go badly from here out...", file.getName());
            }
        }


        LogHelper.info("Loading script headers took {} milliseconds.", System.currentTimeMillis() - timeMillis);

        // Register our preprocessors:
        preprocessorMap.put("json", (filename, input) -> "module.exports = " + input + "; __CraftingHarmonicsInternal.FileProcessor('"
                + filename + "',module.exports);");
        preprocessorMap.put("js", (filename, input) -> "(function() {" + input + "}());");
    }


    public void ReadConfigFile(File file) {
        if(nashorn == null) {
            LogHelper.fatal("Nashorn library isn't loaded; please make sure you have NashornLib in your mods folder.");
        }

        // Load our config...
        String fileContent = getFileContent(file);

        // Process our content...
        String fileExtension = FilenameUtils.getExtension(file.getAbsolutePath());
        if(preprocessorMap.containsKey(fileExtension)) {
            fileContent = preprocessorMap.get(fileExtension).process(file.getName(), fileContent);
        }

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

    private Set<File> getResources(@Nonnull File rootDir) {
        if(!rootDir.isDirectory()) return Collections.singleton(rootDir);

        Set<File> resources = new HashSet<>();
        for (File resource : rootDir.listFiles()) {
            if(resource.isDirectory()) resources.addAll(getResources(resource));
            else resources.add(resource);
        }
        return resources;
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

    @FunctionalInterface
    private static interface IFilePreprocessor {
        String process(String filename, String input);
    }
}
