package org.winterblade.minecraft.harmony.callbacks;

@FunctionalInterface
public interface JavaScriptVarargsCallback {
    void apply(Object... params);
}
