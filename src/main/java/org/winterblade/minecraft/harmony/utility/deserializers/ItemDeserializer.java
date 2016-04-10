package org.winterblade.minecraft.harmony.utility.deserializers;

import net.minecraft.item.Item;
import org.winterblade.minecraft.harmony.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.api.ScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = Item.class)
public class ItemDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        try {
            return ItemRegistry.TranslateToItemStack((String)input).getItem();
        } catch (ItemMissingException e) {
            return null;
        }
    }
}
