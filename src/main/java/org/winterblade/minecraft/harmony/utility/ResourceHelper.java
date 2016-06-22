package org.winterblade.minecraft.harmony.utility;

import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matt on 6/21/2016.
 */
public class ResourceHelper {
    private ResourceHelper() {}


    public static Set<File> getResources(@Nonnull File rootDir) {
        if(!rootDir.isDirectory()) return Collections.singleton(rootDir);

        Set<File> resources = new HashSet<>();
        for (File resource : rootDir.listFiles()) {
            if(resource.isDirectory()) resources.addAll(getResources(resource));
            else resources.add(resource);
        }
        return resources;
    }


    public static Set<File> getResources(URL samples) throws URISyntaxException {
        return getResources(new File(samples.toURI()));
    }

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
