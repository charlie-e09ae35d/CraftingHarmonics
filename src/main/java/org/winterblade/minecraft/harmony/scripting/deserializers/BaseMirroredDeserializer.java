package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;

/**
 * Created by Matt on 5/15/2016.
 */
public abstract class BaseMirroredDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
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

        return DeserializeMirror(mirror);
    }

    protected abstract Object DeserializeMirror(ScriptObjectMirror mirror);
}
