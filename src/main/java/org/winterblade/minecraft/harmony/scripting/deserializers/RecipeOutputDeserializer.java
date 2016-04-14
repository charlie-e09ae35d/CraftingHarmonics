package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;
import org.winterblade.minecraft.harmony.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.api.ScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.crafting.ComponentRegistry;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.RecipeOutput;
import org.winterblade.minecraft.harmony.scripting.ScriptObjectReader;

import java.util.List;

/**
 * Created by Matt on 4/14/2016.
 */
@ScriptObjectDeserializer(deserializes = RecipeOutput.class)
public class RecipeOutputDeserializer implements IScriptObjectDeserializer {

    @Override
    public Object Deserialize(Object input) {
        RecipeOutput output = new RecipeOutput();

        // If we're a basic string...
        if(input instanceof String) {
            try {
                output.setOutputItem(ItemRegistry.TranslateToItemStack((String) input));
            } catch (ItemMissingException e) {
                System.err.println("Couldn't convert '" + input.toString() + "' to a valid item string.");
            }
            return output;
        }

        // Make sure we can continue:
        if(!ScriptObjectMirror.class.isAssignableFrom(input.getClass()) &&
                !ScriptObject.class.isAssignableFrom(input.getClass())) return output;

        ScriptObjectMirror mirror;

        // The first case will probably not happen, but, just in case...
        if(ScriptObjectMirror.class.isAssignableFrom(input.getClass())) {
            mirror = (ScriptObjectMirror) input;
        } else {
            mirror = ScriptUtils.wrap((ScriptObject) input);
        }

        // If we don't have an item... /sigh
        if (!mirror.containsKey("item")) return output;
        Object item = mirror.get("item");

        // Make sure we have a string and somebody's not trying to be clever..
        if(!(item instanceof String)) {
            System.err.println("Couldn't convert '" + item.toString() + "' to a valid item string.");
            return output;
        }

        try {
            output.setOutputItem(ItemRegistry.TranslateToItemStack((String) item));
        } catch (ItemMissingException e) {
            System.err.println("Couldn't convert '" + item.toString() + "' to a valid item string.");
        }

        // Check NBT:
        if (mirror.containsKey("nbt")) {
            NBTTagCompound tag = ScriptObjectReader.convertData(mirror.get("nbt"), NBTTagCompound.class);
            output.getOutputItem().setTagCompound(tag);
        }

        // Check quantity:
        if(mirror.containsKey("quantity")) {
            int qty = ScriptObjectReader.convertData(mirror.get("quantity"), Integer.class);
            output.getOutputItem().stackSize = qty;
        }

        // Check callback:
        // Get our registry data...
        ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                IItemStackTransformer.class
                }, mirror);

        List<IItemStackTransformer> transformers = registry.getComponentsOf(IItemStackTransformer.class);

        if(transformers != null) {
            for(IItemStackTransformer transformer : transformers) {
                output.addTransformer(transformer);

                // Also deal with any implied transformers we have (if any)
                IItemStackTransformer[] impliedTransformers = transformer.getImpliedTransformers();
                for(IItemStackTransformer implied : impliedTransformers) {
                    output.addTransformer(implied);
                }
            }
        }

        return output;
    }
}
