package org.winterblade.minecraft.harmony.utility;

import net.minecraftforge.fml.common.Loader;
import org.winterblade.minecraft.harmony.api.calendar.ICalendarProvider;
import org.winterblade.minecraft.harmony.api.utility.IDependentProvider;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 6/5/2016.
 */
public class TypedProviderRegistry<TProvider extends IDependentProvider> {
    protected final Map<String, TProvider> providers = new HashMap<>();

    /**
     * Does initial registration of the calendar providers
     * @param registeredClasses    The registered classes.
     */
    public void registerProviders(Map<String, Class<TProvider>> registeredClasses) {

        providers.clear();

        for(Map.Entry<String, Class<TProvider>> regClass : registeredClasses.entrySet()) {
            try {
                TProvider provider = regClass.getValue().newInstance();

                if(provider.getDependencyList() != null
                        && !Arrays.stream(provider.getDependencyList()).allMatch(Loader::isModLoaded)) {
                    LogHelper.info("Not registering provider '{}', due to a missing dependency.", provider.getName());
                    continue;
                }

                providers.put(provider.getName(), provider);
                LogHelper.info("Registering provider '{}'.", provider.getName());
            } catch (InstantiationException | IllegalAccessException | NoClassDefFoundError e) {
                LogHelper.warn("Error registering provider '{}'.", regClass.getKey(), e);
            }
        }
    }
}
