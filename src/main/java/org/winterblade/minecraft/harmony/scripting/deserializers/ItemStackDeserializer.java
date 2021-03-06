package org.winterblade.minecraft.harmony.scripting.deserializers;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = ItemStack.class)
public class ItemStackDeserializer implements IScriptObjectDeserializer {
    @Override
    public Object Deserialize(Object input) {
        try {
            return ItemUtility.translateToItemStack((String)input);
        } catch (OperationException e) {
            return null;
        }
    }
}
