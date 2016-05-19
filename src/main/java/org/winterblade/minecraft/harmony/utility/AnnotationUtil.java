package org.winterblade.minecraft.harmony.utility;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Borrowed mostly from mezz's JEI class of the same name; modified to load the name of the operation
 */
public class AnnotationUtil {
    private AnnotationUtil() {

    }

    public static Map<String, Class<BaseRecipeOperation>> getRecipeOperations(@Nonnull ASMDataTable asmDataTable) {
        return getClassMap(asmDataTable, RecipeOperation.class, BaseRecipeOperation.class, "name");
    }

    public static Map<String, Class<Object>> getComponentClasses(@Nonnull ASMDataTable asmDataTable) {
        return getClassMap(asmDataTable, Component.class, Object.class, null);
    }

    @SuppressWarnings("unchecked")
    private static <T,Tk> Map<Tk, Class<T>> getClassMap(@Nonnull ASMDataTable asmDataTable,
                                                        Class<?> annotationClass,
                                                        Class<T> outputClass,
                                                        @Nullable String idParam) {
        String annotationClassName = annotationClass.getCanonicalName();
        Set<ASMDataTable.ASMData> asmTable = asmDataTable.getAll(annotationClassName);

        Map<Tk,Class<T>> instances = new HashMap<>();
        for (ASMDataTable.ASMData asmData : asmTable) {
            try {
                Class<?> asmClass = Class.forName(asmData.getClassName());

                if(!outputClass.isAssignableFrom(asmClass)) {
                    LogHelper.error("Attempted to load '" + asmClass.getSimpleName() +
                            "', but it doesn't implement '" + outputClass.getSimpleName() + "'.");
                    continue;
                }

                // Fall back to name
                Object name = null;

                if(idParam == null) {
                    name = asmData.getClassName();
                } else if(asmData.getAnnotationInfo().containsKey(idParam)) {
                    name = asmData.getAnnotationInfo().get(idParam);
                }

                if(name == null) {
                    LogHelper.error("Attempted to load '" + asmClass.getSimpleName() +
                            "', couldn't find the ID parameter '" + idParam + "' on it.");
                    continue;
                }

                instances.put((Tk) name, (Class<T>) asmClass);
            } catch (NoClassDefFoundError e) {
                LogHelper.warn("Failed to load: " + asmData.getClassName() + " due to a missing dependency.");
            } catch(Exception e) {
                LogHelper.error("Failed to load: " + asmData.getClassName() + ".", e);
            }
        }
        return instances;
    }
}
