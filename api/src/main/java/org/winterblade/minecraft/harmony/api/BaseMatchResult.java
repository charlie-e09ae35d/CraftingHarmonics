package org.winterblade.minecraft.harmony.api;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/12/2016.
 */
public class BaseMatchResult {
    public static final BaseMatchResult False = new BaseMatchResult(false);
    public static final BaseMatchResult True = new BaseMatchResult(true);

    private final boolean result;
    @Nullable
    private final Runnable callback;

    public BaseMatchResult(boolean result) {
        this(result, null);
    }

    public BaseMatchResult(boolean result, @Nullable Runnable callback) {
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
