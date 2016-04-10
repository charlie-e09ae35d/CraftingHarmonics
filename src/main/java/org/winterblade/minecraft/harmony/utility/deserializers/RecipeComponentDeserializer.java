package org.winterblade.minecraft.harmony.utility.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.winterblade.minecraft.harmony.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.api.ScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;
import org.winterblade.minecraft.harmony.utility.ScriptObjectReader;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = RecipeComponent.class)
public class RecipeComponentDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        try {
            return TranslateToItemStack(input);
        } catch (ItemMissingException e) {
            return null;
        }
    }

    /**
     * Translates script data to an item stack
     * @param data  The data to translate
     * @return      The ItemStack requested
     * @throws ItemMissingException When the item cannot be found in the registry.
     */
    private static RecipeComponent TranslateToItemStack(Object data) throws ItemMissingException {
        RecipeComponent component = new RecipeComponent();

        if(data instanceof String) {
            String itemString = (String)data;

            if(ItemRegistry.IsOreDictionaryEntry(itemString)) {
                component.setOreDictName(itemString, ItemRegistry.GetOreDictionaryName(itemString));
            } else {
                component.setItemStack(itemString, ItemRegistry.TranslateToItemStack(itemString));
            }

            return component;
        }

        ScriptObjectMirror item = ScriptUtils.wrap((ScriptObject) data);

        if(!item.hasMember("item")) return null;

        ScriptObjectReader.WriteScriptObjectToClass(item,component);

        return component;
    }
}
