package org.winterblade.minecraft.harmony.utility;

import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Matt on 6/21/2016.
 */
public class ResourceHelper {
    private ResourceHelper() {}


    /**
     * Read the contents of the given file
     * @param file    The file to read
     * @return        The contents, or an empty string if it failed.
     */
    public static String getFileContent(File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (Exception e) {
            LogHelper.error("Error processing file " + file.getPath(),e);
            return "";
        }
    }
}
