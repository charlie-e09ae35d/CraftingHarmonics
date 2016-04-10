package org.winterblade.minecraft.harmony.utility.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.api.ScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.scripting.ScriptExecutionManager;

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
            json = ScriptExecutionManager.getJsonString(input);
        }

        try {
            return JsonToNBT.getTagFromJson(json);
        } catch (NBTException e) {
            System.out.println("Unable to convert '" + json + "' to NBT tag.");
            return new NBTTagCompound();
        }
    }
}
