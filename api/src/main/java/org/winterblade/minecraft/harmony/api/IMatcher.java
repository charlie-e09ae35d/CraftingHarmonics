package org.winterblade.minecraft.harmony.api;

import org.winterblade.minecraft.harmony.api.BaseMatchResult;

public interface IMatcher<TEvt, TResult> {
    BaseMatchResult isMatch(TEvt evt, TResult result);
}
