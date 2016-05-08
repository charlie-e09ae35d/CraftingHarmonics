package org.winterblade.minecraft.harmony.scripting.deserializers;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 5/7/2016.
 */
@ScriptObjectDeserializer(deserializes = Potion.class)
public class PotionDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        return Potion.REGISTRY.getObject(new ResourceLocation(input.toString()));
    }
}
