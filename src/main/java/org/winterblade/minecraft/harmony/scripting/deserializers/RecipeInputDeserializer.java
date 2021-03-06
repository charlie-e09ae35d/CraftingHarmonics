package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;
import org.winterblade.minecraft.harmony.api.IRecipeInputMatcher;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.crafting.matchers.ItemMatcher;
import org.winterblade.minecraft.harmony.crafting.matchers.MetadataMatcher;
import org.winterblade.minecraft.harmony.crafting.matchers.NbtMatcher;
import org.winterblade.minecraft.harmony.crafting.matchers.OreDictionaryMatcher;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.List;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = RecipeInput.class)
public class RecipeInputDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        RecipeInput output = new RecipeInput();

        if(input instanceof String) {
            addItemStringBasedMatchers(output, (String)input);
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
            LogHelper.error("Couldn't convert '" + item.toString() + "' to a valid item string.");
            return output;
        }

        addItemStringBasedMatchers(output, (String)item);

        // And if we still don't have anything, means we still had a bad input string...
        if(RecipeInput.isNullOrEmpty(output)) {
            LogHelper.error("Couldn't convert '" + item.toString() + "' to a valid item string.");
            return output;
        }

        // Get our registry data...
        ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                IRecipeInputMatcher.class,
                IItemStackTransformer.class}, mirror);

        List<IRecipeInputMatcher> matchers = registry.getComponentsOf(IRecipeInputMatcher.class);
        List<IItemStackTransformer> transformers = registry.getComponentsOf(IItemStackTransformer.class);

        // Deal with matchers
        if(matchers != null) {
            for (IRecipeInputMatcher matcher : matchers) {
                // Quick hack; should do a global registration of this later for faster lookup...
                PrioritizedObject priority = matcher.getClass().getAnnotation(PrioritizedObject.class);
                output.addMatcher(matcher, priority.priority());
            }
        }

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

    /**
     * Translates the string into the appropriate set of matchers.
     * @param itemString    The item string to translate
     */
    private void addItemStringBasedMatchers(RecipeInput output, String itemString) {
        if(itemString.trim().equals("")) return;

        if(ItemUtility.isOreDictionaryEntry(itemString)) {
            // This will be literally the only matcher on this object.
            String oreDictName = ItemUtility.getOreDictionaryName(itemString);
            output.addMatcher(
                new OreDictionaryMatcher(oreDictName), Priority.MEDIUM
            );
            output.setFacimileItem(oreDictName);
            return;
        }

        ItemStack item;

        try {
            item = ItemUtility.translateToItemStack(itemString);
        } catch (OperationException e) {
            LogHelper.error("Couldn't convert '" + itemString + "' to a valid item string.");
            return;
        }

        if(item == null) return;
        output.setFacsimileItem(item);

        // These will always be hardcoded here.
        output.addMatcher(new ItemMatcher(item.getItem()), Priority.HIGHEST);
        output.addMatcher(new MetadataMatcher(item.getMetadata()), Priority.HIGHEST);

        if(item.hasTagCompound()) {
            // If we're deserializing from string, pass false to fuzzy and it'll search it
            // based on if our custom tag was injected or not.
            output.addMatcher(new NbtMatcher(item.getTagCompound(), false), Priority.HIGH);
        }
    }
}
