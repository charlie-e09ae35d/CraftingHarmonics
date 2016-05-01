package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.harmony.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = NBTTagCompound.class)
public class NbtTagCompoundDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        String json = "{}";
        if(input instanceof String) {
            json = input.toString();
        } else if(input instanceof ScriptObjectMirror) {
            json = NashornConfigProcessor.getInstance().nashorn.stringifyJsonObject((JSObject) input);
        }

        try {
            return JsonToNBT.getTagFromJson(json);
        } catch (NBTException e) {
            LogHelper.error("Unable to convert '" + json + "' to NBT tag.", e);
            return new NBTTagCompound();
        }
    }
}
