package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.crafting.ComponentRegistry;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.mobs.drops.MobDrop;
import org.winterblade.minecraft.harmony.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.List;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = MobDrop.class)
public class MobDropDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        MobDrop output = new MobDrop();

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

        // If we don't have an item... /sigh
        if (!mirror.containsKey("what")) return output;

        try {
            output.setWhat(ItemRegistry.TranslateToItemStack(mirror.get("what").toString()));
        } catch (ItemMissingException e) {
            LogHelper.error("Couldn't convert '" + mirror.get("what") + "' to a valid item string.");
            return output;
        }

        if(mirror.containsKey("min")) {
            output.setMin((Integer) mirror.get("min"));
        }

        if(mirror.containsKey("max")) {
            output.setMax((Integer) mirror.get("max"));
        }

        if(mirror.containsKey("lootingMultiplier")) {
            output.setLootingMultiplier((Double) mirror.get("lootingMultiplier"));
        }

        // Get our registry data...
        ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                IMobDropMatcher.class}, mirror);

        List<IMobDropMatcher> matchers = registry.getComponentsOf(IMobDropMatcher.class);

        // Deal with matchers
        if(matchers != null) {
            for (IMobDropMatcher matcher : matchers) {
                // Quick hack; should do a global registration of this later for faster lookup...
                PrioritizedObject priority = matcher.getClass().getAnnotation(PrioritizedObject.class);
                output.addMatcher(matcher, priority.priority());
            }
        }

        return output;
    }
}
