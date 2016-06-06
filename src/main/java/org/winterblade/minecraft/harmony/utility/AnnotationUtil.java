package org.winterblade.minecraft.harmony.utility;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.ScriptInterop;
import org.winterblade.minecraft.harmony.api.calendar.CalendarProvider;
import org.winterblade.minecraft.harmony.api.calendar.ICalendarProvider;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.questing.IQuestProvider;
import org.winterblade.minecraft.harmony.api.questing.QuestProvider;
import org.winterblade.minecraft.harmony.api.temperature.ITemperatureProvider;
import org.winterblade.minecraft.harmony.api.temperature.TemperatureProvider;
import org.winterblade.minecraft.harmony.api.tileentities.TileEntityCallback;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.entities.callbacks.BaseEntityCallback;
import org.winterblade.minecraft.harmony.tileentities.BaseTileEntityCallback;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Borrowed mostly from mezz's JEI class of the same name; modified to load the name of the operation
 */
public class AnnotationUtil {
    private final ASMDataTable asmData;

    public AnnotationUtil(ASMDataTable asmData) {
        this.asmData = asmData;
    }

    public Map<String, Class<BasicOperation>> getRecipeOperations() {
        return getClassMap(Operation.class, BasicOperation.class, "name");
    }

    public Map<String, Class<Object>> getComponentClasses() {
        return getClassMap(Component.class, Object.class, null);
    }

    public Map<String, Class<Object>> getInteropClasses() {
        return getClassMap(ScriptInterop.class, Object.class, null);
    }

    public Map<String, Class<BaseEntityCallback>> getEntityCallbacks() {
        return getClassMap(EntityCallback.class, BaseEntityCallback.class, "name");
    }

    public Map<String, Class<BaseTileEntityCallback>> getTileEntityCallbacks() {
        return getClassMap(TileEntityCallback.class, BaseTileEntityCallback.class, "name");
    }

    public Map<String, Class<IQuestProvider>> getQuestProviders() {
        return getClassMap(QuestProvider.class, IQuestProvider.class, null);
    }

    public Map<String, Class<ICalendarProvider>> getCalendarProviders() {
        return getClassMap(CalendarProvider.class, ICalendarProvider.class, null);
    }

    public Map<String, Class<ITemperatureProvider>> getTemperatureProviders() {
        return getClassMap(TemperatureProvider.class, ITemperatureProvider.class, null);
    }

    @SuppressWarnings("unchecked")
    private <T,Tk> Map<Tk, Class<T>> getClassMap(Class<?> annotationClass,
                                                        Class<T> outputClass,
                                                        @Nullable String idParam) {
        String annotationClassName = annotationClass.getCanonicalName();
        Set<ASMDataTable.ASMData> asmTable = asmData.getAll(annotationClassName);

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
