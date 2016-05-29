package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IMobMatcher;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;

import java.util.List;

/**
 * A base for all deserializers that only require matchers
 * @param <TEvt>        The event input (ie: LivingDropsEvent, EntityLivingBase, etc)
 * @param <TResult>     The result object used in matching (ie: ItemStack, PotionEffect)
 * @param <TMatcher>    The matcher to use (ie: IMobDropMatcher, IBlockDropMatcher)
 * @param <T>           The deserialized class itself
 */
public abstract class BaseMatchingDeserializer
        <TEvt, TResult, TMatcher extends IMobMatcher<TEvt, TResult>, T extends BaseEventMatch<TEvt, TResult, TMatcher>> extends BaseComponentDeserializer<T, TMatcher> {
    protected BaseMatchingDeserializer(Class<TMatcher> matcherClass) {
        super(matcherClass);
    }

    @Override
    protected final void update(ScriptObjectMirror mirror, T output, List<TMatcher> matchers) {
        if(matchers != null) {
            for (TMatcher matcher : matchers) {
                PrioritizedObject priorityAnno = matcher.getClass().getAnnotation(PrioritizedObject.class);
                Priority priority = priorityAnno != null ? priorityAnno.priority() : Priority.MEDIUM;
                output.addMatcher(matcher, priority);
            }
        }

        update(mirror, output);

        // If we have an alt match...
        if (!mirror.containsKey("otherwise")) return;

        Object altMatchData = mirror.get("otherwise");

        // Check if we can deserialize it:
        if (!ScriptObjectMirror.class.isAssignableFrom(altMatchData.getClass())) return;

        ScriptObjectMirror altMatchMirror = (ScriptObjectMirror)altMatchData;

        // Try to deserialize it:
        try {
            T altMatch = newInstance(altMatchMirror.containsKey("type") ? altMatchMirror.get("type").toString() : "");
            NashornConfigProcessor.getInstance().nashorn.parseScriptObject(altMatchMirror, altMatch);
            output.setAltMatch(altMatch);
        } catch(Exception ex) {
            LogHelper.warn("Unable to deserialize 'otherwise' for this object.");
        }

    }

    protected abstract void update(ScriptObjectMirror mirror, T output);
}
