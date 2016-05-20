package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.api.crafting.components.RecipeComponent;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = RecipeComponent.class)
public class RecipeComponentDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        try {
            return TranslateToItemStack(input);
        } catch (OperationException e) {
            return null;
        }
    }

    /**
     * Translates script data to an item stack
     * @param data  The data to translate
     * @return      The ItemStack requested
     * @throws OperationException When the item cannot be found in the registry.
     */
    private static RecipeComponent TranslateToItemStack(Object data) throws OperationException {
        RecipeComponent component = new RecipeComponent();

        if(data instanceof String) {
            String itemString = (String)data;

            if(ItemUtility.isOreDictionaryEntry(itemString)) {
                component.setOreDictName(itemString, ItemUtility.getOreDictionaryName(itemString));
            } else {
                component.setItemStack(itemString, ItemUtility.translateToItemStack(itemString));
            }

            return component;
        }

        ScriptObjectMirror item = ScriptUtils.wrap((ScriptObject) data);

        if(!item.hasMember("item")) return null;

        NashornConfigProcessor.getInstance().nashorn.parseScriptObject(item,component);

        return component;
    }
}
