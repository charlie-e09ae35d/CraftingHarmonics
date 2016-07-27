package org.winterblade.minecraft.harmony.scripting.deserializers;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 7/21/2016.
 */
@ScriptObjectDeserializer(deserializes = Fluid.class)
public class FluidDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        try {
            return FluidRegistry.getFluid(input.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
