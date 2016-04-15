package org.winterblade.minecraft.harmony.scripting;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.logging.log4j.Logger;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.scripting.api.INashornMod;
import org.winterblade.minecraft.scripting.api.IScriptContext;
import org.winterblade.minecraft.scripting.api.NashornMod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Matt on 4/8/2016.
 */
@NashornMod
public class NashornConfigProcessor implements INashornMod {
    private final static NashornConfigProcessor instance = new NashornConfigProcessor();

    private final String header;
    private IScriptContext nashorn;

    public NashornConfigProcessor() {
        // Assign out our script header file...
        String tempHeader;
        try {
            tempHeader = Resources.toString(Resources.getResource("scripts/InternalFileProcessor.js"), Charsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Unable to load file processing header; things will go badly from here out...");
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
            CraftingHarmonicsMod.logger.fatal("Nashorn library isn't loaded; please make sure you have NashornLib in your mods folder.");
        }

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

    @Override
    public Logger getLogger() {
        return CraftingHarmonicsMod.logger;
    }

    @Override
    public String[] getAllowedPackageRoots() {
        return new String[] { "org.winterblade.minecraft.harmony"};
    }

    @Override
    public void onScriptContextCreated(IScriptContext iScriptContext) {
        NashornConfigProcessor.getInstance().init(iScriptContext);
    }
}
