package org.winterblade.minecraft.harmony.api.drops;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/12/2016.
 */
public class BaseDropMatchResult {
    public static final BaseDropMatchResult False = new BaseDropMatchResult(false);
    public static final BaseDropMatchResult True = new BaseDropMatchResult(true);

    private final boolean result;
    @Nullable
    private final Runnable callback;

    public BaseDropMatchResult(boolean result) {
        this(result, null);
    }

    public BaseDropMatchResult(boolean result, @Nullable Runnable callback) {
        this.result = result;
        this.callback = callback;
    }

    public boolean isMatch() {
        return result;
    }

    @Nullable
    public Runnable getCallback() {
        return callback;
    }
}
