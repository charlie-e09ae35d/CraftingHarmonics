package org.winterblade.minecraft.harmony.scripting;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.internal.runtime.ConsString;
import org.apache.logging.log4j.Logger;
import org.winterblade.minecraft.scripting.ScriptExecutionManager;

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

    private final String header;
    private ScriptEngine nashorn;

    private NashornConfigProcessor() {
        // Assign out our script header file...
        String tempHeader;
        try {
            tempHeader = Resources.toString(Resources.getResource("scripts/InternalFileProcessor.js"), Charsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Unable to load file processing header; things will go badly from here out...");
            tempHeader = "";
        }

        header = tempHeader;

        // Warn users they're about to see an error reported by FML... FML.
        System.out.println("The below warning regarding System.exit() is perfectly normal; this is unfortunately an " +
                "internal part of the Nashorn engine that I can't suppress, however, it is not callable from within " +
                "scripts.");
    }

    /**
     * Gets the instance of this singleton
     * @return  The instance of this singleton
     */
    public static NashornConfigProcessor getInstance() {
        return instance;
    }

    public void init(Logger logger) {
        nashorn = ScriptExecutionManager.getNewContext(logger, new String[]{"org.winterblade.minecraft.harmony"});

        // Actually try and load our script header into Nashorn.
        try {
            nashorn.eval(header);
        } catch (ScriptException e) {
            System.err.println("Error processing script header file; please report this issue: " + e.getMessage());
        }
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
}
