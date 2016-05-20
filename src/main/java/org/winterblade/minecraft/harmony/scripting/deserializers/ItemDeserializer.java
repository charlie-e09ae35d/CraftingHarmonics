package org.winterblade.minecraft.harmony.scripting.deserializers;

import net.minecraft.item.Item;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = Item.class)
public class ItemDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        try {
            return ItemUtility.translateToItemStack((String)input).getItem();
        } catch (ItemMissingException e) {
            return null;
        }
    }
}
