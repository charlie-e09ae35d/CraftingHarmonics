package org.winterblade.minecraft.harmony.scripting.deserializers;

import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.utility.OreDictionaryItemStack;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

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
        return ItemUtility.IsOreDictionaryEntry(data)
                ? new OreDictionaryItemStack(data, ItemUtility.GetOreDictionaryName(data))
                : new OreDictionaryItemStack(data, ItemUtility.TranslateToItemStack(data));
    }
}
