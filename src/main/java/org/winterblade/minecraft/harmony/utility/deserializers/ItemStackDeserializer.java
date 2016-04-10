package org.winterblade.minecraft.harmony.utility.deserializers;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.api.ScriptObjectDeserializer;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = ItemStack.class)
public class ItemStackDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        try {
            return ItemRegistry.TranslateToItemStack((String)input);
        } catch (ItemMissingException e) {
            return null;
        }
    }
}
