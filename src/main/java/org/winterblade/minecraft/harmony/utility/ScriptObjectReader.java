package org.winterblade.minecraft.harmony.utility;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.objects.NativeObject;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.crafting.components.BaseRecipeComponent;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by Matt on 4/8/2016.
 */
public class ScriptObjectReader {
    /**
     * Reflects the Java object passed in and writes relevant data from the script object to fields on the Java object.
     * @param data      The script object
     * @param writeTo   The Java object
     */
    public static void WriteScriptObjectToClass(ScriptObjectMirror data, Object writeTo) {
        Class cls = writeTo.getClass();

        do {
            Field[] fields = cls.getDeclaredFields();

            for (Field field : fields) {
                int modifiers = field.getModifiers();
                String fieldName = field.getName();

                // Don't bother deserializing in these cases:
                if (Modifier.isFinal(modifiers)
                        || Modifier.isTransient(modifiers)
                        || Modifier.isAbstract(modifiers)
                        || !data.hasMember(fieldName)) continue;

                // Get the field from our data...
                Object o = data.get(fieldName);

                try {
                    // Make sure we can actually write to the field...
                    field.setAccessible(true);

                    // If we have an item stack, process the inbound data through the registry...
                    if (OreDictionaryItemStack[].class.isAssignableFrom(field.getType())) {
                        Object[] items = (Object[]) ScriptUtils.convert(o, Object[].class);
                        OreDictionaryItemStack[] stacks = new OreDictionaryItemStack[items.length];
                        for (int i = 0; i < items.length; i++) {
                            stacks[i] = TranslateToOreDictionaryItemStack(items[i]);
                        }
                        field.set(writeTo, stacks);
                    } else if(OreDictionaryItemStack.class.isAssignableFrom(field.getType())) {
                        field.set(writeTo, TranslateToOreDictionaryItemStack(o));
                    } else if (ItemStack[].class.isAssignableFrom(field.getType())) {
                        Object[] items = (Object[])ScriptUtils.convert(o, Object[].class);
                        ItemStack[] stacks = new ItemStack[items.length];
                        for (int i = 0; i < items.length; i++) {
                            stacks[i] = TranslateToItemStack(items[i]);
                        }
                        field.set(writeTo, stacks);
                    } else if (ItemStack.class.isAssignableFrom(field.getType())) {
                        field.set(writeTo, TranslateToItemStack(o));
                    } else if (NBTTagCompound.class.isAssignableFrom(field.getType())) {
                        String json = "{}";
                        if(o instanceof String) {
                            json = o.toString();
                        } else if(o instanceof ScriptObjectMirror) {
                            json = data.callMember("getJson", o).toString();
                        }

                        try {
                            field.set(writeTo, JsonToNBT.getTagFromJson(json));
                        } catch (NBTException e) {
                            System.out.println("Unable to convert '" + json + "' to NBT tag.");
                        }
                    } else {
                        field.set(writeTo, ScriptUtils.convert(o, field.getType()));
                    }
                } catch (Exception e) {
                    System.err.println("Unable to deserialize '" + fieldName + "' from the provided data: " + e.getMessage());
                }
            }

            cls = cls.getSuperclass();
        } while(cls != null);
    }
    /**
     * Translates script data to an item stack
     * @param data  The data to translate
     * @return      The ItemStack requested
     * @throws ItemMissingException When the item cannot be found in the registry.
     */
    public static ItemStack TranslateToItemStack(Object data) throws ItemMissingException {
        if(data instanceof String) return ItemRegistry.TranslateToItemStack((String)data);
        System.out.println(data.getClass());
        ScriptObjectMirror item = ScriptUtils.wrap((ScriptObject) data);

        if(!item.hasMember("item")) return null;

        BaseRecipeComponent component = new BaseRecipeComponent();
        ScriptObjectReader.WriteScriptObjectToClass(item,component);

        return component.getItemStack();
    }
    /**
     * Translates script data to either an ore dictionary name or an item stack
     * @param data  The item
     * @return      The OreDictionaryItemStack
     */
    public static OreDictionaryItemStack TranslateToOreDictionaryItemStack(Object data) throws ItemMissingException {
        if(data instanceof String) {
            String itemString = (String)data;

            return ItemRegistry.IsOreDictionaryEntry(itemString)
                    ? new OreDictionaryItemStack(ItemRegistry.GetOreDictionaryName(itemString))
                    : new OreDictionaryItemStack(ItemRegistry.TranslateToItemStack(itemString));
        }

        return new OreDictionaryItemStack(TranslateToItemStack(data));
    }
}
