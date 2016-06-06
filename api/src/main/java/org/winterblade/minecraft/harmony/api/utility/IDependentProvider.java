package org.winterblade.minecraft.harmony.api.utility;

/**
 * Created by Matt on 6/5/2016.
 */
public interface IDependentProvider {
    /**
     * Gets the name of this provider.
     * @return  The name of the provider, used to reference it in matchers/callbacks.
     */
    String getName();

    /**
     * Get a list of mod dependencies for this provider
     * @return  A string array of mod dependencies.
     */
    String[] getDependencyList();
}
