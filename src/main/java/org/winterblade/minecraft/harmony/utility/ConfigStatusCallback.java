package org.winterblade.minecraft.harmony.utility;

import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Matt on 7/1/2016.
 */
@FunctionalInterface
public interface ConfigStatusCallback {
    void call(boolean success, @Nullable List<NashornConfigProcessor.ScriptError> errors);
}
