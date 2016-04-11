package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.*;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.crafting.RecipeInputMatcherRegistry;
import org.winterblade.minecraft.harmony.crafting.matchers.ItemMatcher;
import org.winterblade.minecraft.harmony.crafting.matchers.MetadataMatcher;
import org.winterblade.minecraft.harmony.crafting.matchers.NbtMatcher;
import org.winterblade.minecraft.harmony.crafting.matchers.OreDictionaryMatcher;
import org.winterblade.minecraft.harmony.crafting.transformers.ReplaceOnCraftTransformer;
import org.winterblade.minecraft.harmony.crafting.transformers.ReturnOnCraftTransformer;
import org.winterblade.minecraft.harmony.scripting.ScriptObjectReader;

import java.util.List;
import java.util.Map;

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
            System.err.println("Couldn't convert '" + item.toString() + "' to a valid item string.");
            return output;
        }

        addItemStringBasedMatchers(output, (String)item);

        // And if we still don't have anything, means we still had a bad input string...
        if(RecipeInput.isNullOrEmpty(output)) {
            System.err.println("Couldn't convert '" + item.toString() + "' to a valid item string.");
            return output;
        }

        Map<IRecipeInputMatcher, Priority> matchers = RecipeInputMatcherRegistry.GetMatchersFrom(mirror);

        for(Map.Entry<IRecipeInputMatcher, Priority> kv : matchers.entrySet()) {
            output.addMatcher(kv.getKey(), kv.getValue());
        }

        // Add the transformers hardcoded here.
        if(mirror.containsKey("returnOnCraft") && (Boolean)mirror.get("returnOnCraft")) {
            output.addTransformer(new ReturnOnCraftTransformer());
        }

        if(mirror.containsKey("replaceOnCraft")) {
            ItemStack stack = ScriptObjectReader.convertData(mirror.get("replaceOnCraft"), ItemStack.class);

            if(stack != null) {
                output.addTransformer(new ReplaceOnCraftTransformer(stack));
            }
        }

        return output;
    }

    /**
     * Translates the string into the appropriate set of matchers.
     * @param itemString    The item string to translate
     * @return              The RecipeInput that matches the string.
     */
    private void addItemStringBasedMatchers(RecipeInput output, String itemString) {
        if(itemString.trim().equals("")) return;

        if(ItemRegistry.IsOreDictionaryEntry(itemString)) {
            // This will be literally the only matcher on this object.
            output.addMatcher(
                new OreDictionaryMatcher(ItemRegistry.GetOreDictionaryName(itemString)), Priority.MEDIUM
            );
            return;
        }

        ItemStack item;

        try {
            item = ItemRegistry.TranslateToItemStack(itemString);
        } catch (ItemMissingException e) {
            System.err.println("Couldn't convert '" + itemString + "' to a valid item string.");
            return;
        }

        if(item == null) return;

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
