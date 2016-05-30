package org.winterblade.minecraft.harmony.api.questing;

import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 5/29/2016.
 */
public enum QuestStatus {
    INVALID, ACTIVE, LOCKED, COMPLETE, CLOSED;

    @ScriptObjectDeserializer(deserializes = QuestStatus.class)
    public static class Deserializer implements IScriptObjectDeserializer {

        @Override
        public Object Deserialize(Object input) {
            if(!String.class.isAssignableFrom(input.getClass())) return null;
            return QuestStatus.valueOf(input.toString().toUpperCase());
        }
    }
}
