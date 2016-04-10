package org.winterblade.minecraft.harmony.utility.deserializers;

import org.winterblade.minecraft.harmony.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.api.ScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.utility.OreDictionaryItemStack;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = OreDictionaryItemStack.class)
public class OreDictionaryItemStackDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        try {
            return TranslateToOreDictionaryItemStack((String)input);
        } catch (ItemMissingException e) {
            return null;
        }
    }

    /**
     * Generate an OreDictionaryItemStack from the given info
     * @param data  The string to parse
     * @return      The OreDictionaryItemStack
     * @throws ItemMissingException If the item couldn't be found.
     */
    public static OreDictionaryItemStack TranslateToOreDictionaryItemStack(String data) throws ItemMissingException {
        return ItemRegistry.IsOreDictionaryEntry(data)
                ? new OreDictionaryItemStack(data, ItemRegistry.GetOreDictionaryName(data))
                : new OreDictionaryItemStack(data, ItemRegistry.TranslateToItemStack(data));
    }
}
