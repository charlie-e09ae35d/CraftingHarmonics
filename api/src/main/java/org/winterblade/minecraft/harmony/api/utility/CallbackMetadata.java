package org.winterblade.minecraft.harmony.api.utility;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

/**
 * Created by Matt on 6/7/2016.
 */
public abstract class CallbackMetadata {
    protected final WeakReference<Object> source;

    protected CallbackMetadata(Object source) {
        this.source = new WeakReference<>(source);
    }

    /**
     * Get the original source of the event
     * @param outputClass    The desired output class
     * @param <T>            The desired output class
     * @return               The original source, as the given type; null if it's not assignable to the given type.
     */
    @Nullable
    public <T> T getSourceAs(Class<T> outputClass) {
        Object output = source.get();
        if(output == null || !outputClass.isAssignableFrom(output.getClass())) return null;
        return outputClass.cast(output);
    }
}
