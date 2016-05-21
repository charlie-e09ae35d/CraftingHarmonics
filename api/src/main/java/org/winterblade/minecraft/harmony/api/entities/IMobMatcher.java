package org.winterblade.minecraft.harmony.api.entities;

import org.winterblade.minecraft.harmony.api.BaseMatchResult;

public interface IMobMatcher<TEvt, TResult> {
    BaseMatchResult isMatch(TEvt evt, TResult result);
}
