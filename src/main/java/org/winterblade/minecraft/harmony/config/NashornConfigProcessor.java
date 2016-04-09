package org.winterblade.minecraft.harmony.config;

import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.internal.objects.NativeArguments;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/8/2016.
 */
public class NashornConfigProcessor {
    private final static NashornConfigProcessor instance = new NashornConfigProcessor();

    private NashornConfigProcessor() {}

    /**
     * Gets the instance of this singleton
     * @return  The instance of this singleton
     */
    public static NashornConfigProcessor getInstance() {
        return instance;
    }

    private final static String scriptHeader = "var module = { exports: {} }; " +
            "var __CraftingHarmonicsInternalFileProcessor = function() {" +
            "   print(module.exports.name);" +
            "}; " +
            "print = function() {" +
            "   Java.type('org.winterblade.minecraft.harmony.config.NashornConfigProcessor').log(arguments);" +
            "}; ";

    public static List<ConfigSet> ReadConfigFile(File file) {
        /**
         * This is all going to be hard and fast until I get a file processed.
         */
        // Wrap and retreive the content...
        String fileContent = scriptHeader + "module.exports = ";
        try {
            fileContent += new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (Exception e) {
            System.err.println("Error processing Set file " + file.getPath() + ": " + e.getMessage());
            return new ArrayList<>();
        }

        System.out.println("The below warning regarding System.exit() is perfectly normal; this is unfortunately an " +
                "internal part of the Nashorn engine that I can't suppress, however, it is not callable from within " +
                "scripts.");

        final NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        final ScriptEngine nashorn = factory.getScriptEngine();
        final Bindings bindings = nashorn.getBindings(ScriptContext.ENGINE_SCOPE);

        // Remove most of our bindings
        bindings.remove("load");
        bindings.remove("loadWithNewGlobal");
        bindings.remove("exit");
        bindings.remove("quit");

        try {
            nashorn.eval(fileContent);
        } catch (Exception e) {
            System.err.println("Error processing Set file " + file.getPath() + ": " + e.getMessage());
            return new ArrayList<>();
        }

        Invocable script = (Invocable) nashorn;

        try {
            script.invokeFunction("__CraftingHarmonicsInternalFileProcessor");
        } catch (Exception e) {
            System.err.println("Error processing Set file " + file.getPath() + ": " + e.getMessage());
            return new ArrayList<>();
        }

        return new ArrayList<>();
    }

    private static class ScriptExecutionSandbox implements ClassFilter {

        @Override
        public boolean exposeToScripts(String cls) {
            // TODO: Potentially allow mods to add on to this.
            return cls.startsWith("org.winterblade.minecraft")
                    || cls.startsWith("net.minecraft")
                    || cls.startsWith("net.minecraftforge");
        }
    }

    public static void log(NativeArguments args) {
        for(Object o : args.getArray().asObjectArray()) {
            System.out.println("Script: " + o.toString());
        }
    }
}
