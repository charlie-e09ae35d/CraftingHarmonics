package org.winterblade.minecraft.harmony.callbacks.mobs;

import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptFunction;
import org.winterblade.minecraft.harmony.api.IEntityCallback;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 5/22/2016.
 */
@ScriptObjectDeserializer(deserializes = IEntityCallback.class)
public class EntityCallbackDeserializer implements IScriptObjectDeserializer {
    private static final CommandCallback.Deserializer COMMAND_CALLBACK_DESERIALIZER = new CommandCallback.Deserializer();

    @Override
    public Object Deserialize(Object input) {
        // Method callback
        if(ScriptFunction.class.isAssignableFrom(input.getClass())) {
            try {
                return ScriptUtils.convert(input, IEntityCallback.class);
            } catch (Exception e) {
                LogHelper.error("Unable to convert given callback function into IEntityCallback", e);
                return null;
            }
        }

        // TODO: Actually check the type and do the proper conversion.
        return COMMAND_CALLBACK_DESERIALIZER.Deserialize(input);
    }
}
