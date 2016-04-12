package org.winterblade.minecraft.harmony.utility;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.objectweb.asm.Type;
import org.winterblade.minecraft.harmony.api.*;

import javax.annotation.Nonnull;
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

    public static Map<Type, Class<IScriptObjectDeserializer>> getScriptObjectDeserializers(@Nonnull ASMDataTable asmDataTable) {
        return getClassMap(asmDataTable, ScriptObjectDeserializer.class, IScriptObjectDeserializer.class, "deserializes");
    }

    public static Map<ArrayList<String>, Class<Object>> getComponentClasses(@Nonnull ASMDataTable asmDataTable) {
        return getClassMap(asmDataTable, Component.class, Object.class, "properties");
    }

    @SuppressWarnings("unchecked")
    private static <T,Tk> Map<Tk, Class<T>> getClassMap(@Nonnull ASMDataTable asmDataTable,
                                                        Class<?> annotationClass,
                                                        Class<T> outputClass,
                                                        String idParam) {
        String annotationClassName = annotationClass.getCanonicalName();
        Set<ASMDataTable.ASMData> asmTable = asmDataTable.getAll(annotationClassName);

        Map<Tk,Class<T>> instances = new HashMap<>();
        for (ASMDataTable.ASMData asmData : asmTable) {
            try {
                Class<?> asmClass = Class.forName(asmData.getClassName());

                if(!outputClass.isAssignableFrom(asmClass)) {
                    System.err.println("Attempted to load '" + asmClass.getSimpleName() +
                            "', but it doesn't implement '" + outputClass.getSimpleName() + "'.");
                    continue;
                }

                // Fall back to name
                Object name = null;

                if(asmData.getAnnotationInfo().containsKey(idParam)) {
                    name = asmData.getAnnotationInfo().get(idParam);
                }

                if(name == null) {
                    System.err.println("Attempted to load '" + asmClass.getSimpleName() +
                            "', couldn't find the ID parameter '" + idParam + "' on it.");
                    continue;
                }

                instances.put((Tk) name, (Class<T>) asmClass);
            } catch (ClassNotFoundException e) {
                System.err.println("Failed to load: " + asmData.getClassName() + ".\n" + Arrays.toString(e.getStackTrace()));
            }
        }
        return instances;
    }
}