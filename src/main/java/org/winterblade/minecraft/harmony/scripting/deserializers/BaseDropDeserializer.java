package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.IBaseDropMatcher;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.drops.BaseDrop;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;

import java.util.List;

/**
 * Created by Matt on 5/12/2016.
 */
abstract class BaseDropDeserializer <TEvt, TMatcher extends IBaseDropMatcher<TEvt>, T extends BaseDrop<TEvt, TMatcher>>
        extends BaseComponentDeserializer<T, TMatcher>
        implements IScriptObjectDeserializer {

    BaseDropDeserializer(Class<TMatcher> matcherClass) {
        super(matcherClass);
    }

    protected abstract T newInstance();

    protected void update(ScriptObjectMirror mirror, T output) {
        // If no updates are required, don't make them override it.
    }

    @Override
    protected void update(ScriptObjectMirror mirror, T output, List<TMatcher> matchers) {
        // If have a drop...
        if (mirror.containsKey("what")) {
            try {
                output.setWhat(ItemUtility.translateToItemStack(mirror.get("what").toString()));
            } catch (OperationException e) {
                LogHelper.error("Couldn't convert '" + mirror.get("what") + "' to a valid item string.");
                return;
            }
        }

        if(mirror.containsKey("min")) {
            output.setMin((Integer) mirror.get("min"));
        }

        if(mirror.containsKey("max")) {
            output.setMax((Integer) mirror.get("max"));
        }

        // Deal with matchers
        if(matchers != null) {
            for (TMatcher matcher : matchers) {
                PrioritizedObject priorityAnno = matcher.getClass().getAnnotation(PrioritizedObject.class);
                Priority priority = priorityAnno != null ? priorityAnno.priority() : Priority.MEDIUM;
                output.addMatcher(matcher, priority);
            }
        }

        // Allow the actual deserializer to do its work:
        update(mirror, output);
    }
}

