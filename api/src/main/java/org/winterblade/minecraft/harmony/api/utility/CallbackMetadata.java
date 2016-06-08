package org.winterblade.minecraft.harmony.api.utility;

/**
 * Created by Matt on 6/7/2016.
 */
public abstract class CallbackMetadata {
    protected final Object source;

    protected CallbackMetadata(Object source) {
        this.source = source;
    }
}
