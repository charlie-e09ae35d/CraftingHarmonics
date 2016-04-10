package org.winterblade.minecraft.harmony.utility.deserializers;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.ScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.crafting.matchers.ItemMatcher;
import org.winterblade.minecraft.harmony.crafting.matchers.MetadataMatcher;
import org.winterblade.minecraft.harmony.crafting.matchers.OreDictionaryMatcher;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = RecipeInput.class)
public class RecipeInputDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        RecipeInput output = new RecipeInput();

        if(input instanceof String) {
            ItemStack item;
            String itemString = (String)input;

            if(itemString.trim().equals("")) return output;

            if(ItemRegistry.IsOreDictionaryEntry(itemString)) {
                // This will be literally the only matcher on this object.
                output.addMatcher(
                    new OreDictionaryMatcher(ItemRegistry.GetOreDictionaryName(itemString)), Priority.MEDIUM
                );
                return output;
            }

            try {
                item = ItemRegistry.TranslateToItemStack(itemString);
            } catch (ItemMissingException e) {
                System.err.println("Error creating recipe input for '" + input +"'.");
                return output;
            }

            if(item == null) return output;

            // These will always be hardcoded here.
            output.addMatcher(new ItemMatcher(item.getItem()), Priority.HIGHEST);
            output.addMatcher(new MetadataMatcher(item.getMetadata()), Priority.HIGHEST);
            // TODO: Add NBT matchers

            return output;
        }

        // TODO: Add conversion for a real object here.
        return output;
    }
}
