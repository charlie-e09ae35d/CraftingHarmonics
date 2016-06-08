package org.winterblade.minecraft.harmony.api.utility;

import javax.annotation.Nullable;

/**
 * Created by Matt on 6/7/2016.
 */
public abstract class CallbackMetadata {
    protected final Object source;

    protected CallbackMetadata(Object source) {
        this.source = source;
    }

    /**
     * Get the original source of the event
     * @param outputClass    The desired output class
     * @param <T>            The desired output class
     * @return               The original source, as the given type; null if it's not assignable to the given type.
     */
    @Nullable
    public <T> T getSourceAs(Class<T> outputClass) {
        if(!outputClass.isAssignableFrom(source.getClass())) return null;
        return outputClass.cast(source);
    }
}
