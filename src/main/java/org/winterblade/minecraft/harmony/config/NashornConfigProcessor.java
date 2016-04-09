package org.winterblade.minecraft.harmony.config;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.internal.runtime.ConsString;

import javax.script.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Matt on 4/8/2016.
 */
public class NashornConfigProcessor {
    private final static NashornConfigProcessor instance = new NashornConfigProcessor();

    private final NashornScriptEngineFactory factory;
    private final ScriptEngine nashorn;

    private NashornConfigProcessor() {
        // Assign out our script header file...
        String header;
        try {
            header = Resources.toString(Resources.getResource("scripts/InternalFileProcessor.js"), Charsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Unable to load file processing header; things will go badly from here out...");
            header = "";
        }

        // Warn users they're about to see an error reported by FML... FML.
        System.out.println("The below warning regarding System.exit() is perfectly normal; this is unfortunately an " +
                "internal part of the Nashorn engine that I can't suppress, however, it is not callable from within " +
                "scripts.");

        factory = new NashornScriptEngineFactory();
        nashorn = factory.getScriptEngine();
        final Bindings bindings = nashorn.getBindings(ScriptContext.ENGINE_SCOPE);

        // Actually try and load our script header into Nashorn.
        try {
            nashorn.eval(header);
        } catch (ScriptException e) {
            System.err.println("Error processing script header file; please report this issue: " + e.getMessage());
        }

        // Remove most of our bindings
        bindings.remove("load");
        bindings.remove("loadWithNewGlobal");
        bindings.remove("exit");
        bindings.remove("quit");
    }

    /**
     * Gets the instance of this singleton
     * @return  The instance of this singleton
     */
    public static NashornConfigProcessor getInstance() {
        return instance;
    }


    public void ReadConfigFile(File file) {
        String fileContent = getJsonFileContent(file);
        if (fileContent == null) return;

        try {
            nashorn.eval(fileContent);
        } catch (Exception e) {
            System.err.println("Error processing Set file " + file.getPath() + ": " + e.getMessage());
        }
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
            System.err.println("Error processing Set file " + file.getPath() + ": " + e.getMessage());
            return null;
        }
        fileContent += "; __CraftingHarmonicsInternal.FileProcessor('" + file.getName() + "',module.exports);";
        return fileContent;
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

    public static void log(ConsString args) {
//        for(Object o : args.getArray().asObjectArray()) {
//            System.out.println("Script: " + o.toString());
//        }
        System.out.println(args.toString());
    }
}
