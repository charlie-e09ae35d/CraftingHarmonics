package org.winterblade.minecraft.harmony.utility;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Borrowed mostly from mezz's JEI class of the same name; modified to load the name of the operation
 */
public class AnnotatedInstanceUtil {
    private AnnotatedInstanceUtil() {

    }

    public static Map<String, Class> getRecipeOperations(@Nonnull ASMDataTable asmDataTable) {
        return getInstances(asmDataTable, RecipeOperation.class, IRecipeOperation.class);
    }

    private static <T> Map<String, Class> getInstances(@Nonnull ASMDataTable asmDataTable, Class annotationClass, Class<T> instanceClass) {
        String annotationClassName = annotationClass.getCanonicalName();
        Set<ASMDataTable.ASMData> asmTable = asmDataTable.getAll(annotationClassName);

        Map<String,Class> instances = new HashMap<>();
        for (ASMDataTable.ASMData asmData : asmTable) {
            try {
                Class<?> asmClass = Class.forName(asmData.getClassName());

                if(!instanceClass.isAssignableFrom(asmClass)) {
                    System.err.println("Attempted to load '" + asmClass.getSimpleName() + "', but it doesn't implement '" + instanceClass.getSimpleName() + "'.");
                    continue;
                }

                // Fall back to name
                String name = asmData.getClassName();

                if(asmData.getAnnotationInfo().containsKey("name")) {
                    name = asmData.getAnnotationInfo().get("name").toString();
                }

                System.out.println("Loading '" + asmClass.getSimpleName() + "' for recipe type " + name);
                instances.put(name.toLowerCase(), asmClass);
            } catch (ClassNotFoundException e) {
                System.err.println("Failed to load: " + asmData.getClassName() + ".\n" + e.getStackTrace());
            }
        }
        return instances;
    }
}
