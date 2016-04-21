package org.winterblade.minecraft.harmony.scripting.deserializers;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Deserialize string 'fluid:amount' into a FluidStack
 */
@ScriptObjectDeserializer(deserializes = FluidStack.class)
public class FluidStackDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        try {
            String[] parts = input.toString().split(":");

            return FluidRegistry.getFluidStack(parts[0], Integer.parseInt(parts[1]));
        } catch (Exception e) {
            return null;
        }
    }
}
