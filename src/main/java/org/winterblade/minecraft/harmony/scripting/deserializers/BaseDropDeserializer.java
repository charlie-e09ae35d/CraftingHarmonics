package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.IBaseDropMatcher;
import org.winterblade.minecraft.harmony.crafting.ComponentRegistry;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.drops.BaseDrop;
import org.winterblade.minecraft.harmony.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;

import java.util.List;

/**
 * Created by Matt on 5/12/2016.
 */
public abstract class BaseDropDeserializer <TEvt, TMatcher extends IBaseDropMatcher<TEvt>, T extends BaseDrop<TEvt, TMatcher>>
        implements IScriptObjectDeserializer {
    private final Class<TMatcher> matcherClass;

    protected BaseDropDeserializer(Class<TMatcher> matcherClass) {
        this.matcherClass = matcherClass;
    }

    @Override
    public final Object Deserialize(Object input) {
        T output = newInstance();

        // Make sure we can continue:
        if(!ScriptObjectMirror.class.isAssignableFrom(input.getClass()) &&
                !ScriptObject.class.isAssignableFrom(input.getClass())) return null;

        ScriptObjectMirror mirror;

        // The first case will probably not happen, but, just in case...
        if(ScriptObjectMirror.class.isAssignableFrom(input.getClass())) {
            mirror = (ScriptObjectMirror) input;
        } else {
            mirror = ScriptUtils.wrap((ScriptObject) input);
        }

        // If have a mob...
        if (mirror.containsKey("what")) {
            try {
                output.setWhat(ItemRegistry.TranslateToItemStack(mirror.get("what").toString()));
            } catch (ItemMissingException e) {
                LogHelper.error("Couldn't convert '" + mirror.get("what") + "' to a valid item string.");
                return output;
            }
        }

        if(mirror.containsKey("min")) {
            output.setMin((Integer) mirror.get("min"));
        }

        if(mirror.containsKey("max")) {
            output.setMax((Integer) mirror.get("max"));
        }

        // Get our registry data...
        ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                matcherClass}, mirror);

        List<TMatcher> matchers = registry.getComponentsOf(matcherClass);

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

        return output;
    }

    protected abstract T newInstance();

    protected void update(ScriptObjectMirror mirror, T drop) {
        // If no updates are required, don't make them override it.
    }
}
