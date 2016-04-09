package org.winterblade.minecraft.harmony.utility;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;
import org.winterblade.minecraft.harmony.scripting.ScriptExecutionManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
                    // If we have an item stack, process the inbound data through the registry...
                    if (OreDictionaryItemStack[].class.isAssignableFrom(field.getType())) {
                        Object[] items = (Object[]) ScriptUtils.convert(o, Object[].class);
                        OreDictionaryItemStack[] stacks = new OreDictionaryItemStack[items.length];
                        for (int i = 0; i < items.length; i++) {
                            stacks[i] = TranslateToOreDictionaryItemStack((String)items[i]);
                        }
                        updateField(cls, field, writeTo, stacks);
                    } else if(OreDictionaryItemStack.class.isAssignableFrom(field.getType())) {
                        updateField(cls, field, writeTo, TranslateToOreDictionaryItemStack((String)o));
                    } else if (RecipeComponent[].class.isAssignableFrom(field.getType())) {
                        Object[] items = (Object[])ScriptUtils.convert(o, Object[].class);
                        RecipeComponent[] stacks = new RecipeComponent[items.length];
                        for (int i = 0; i < items.length; i++) {
                            stacks[i] = TranslateToItemStack(items[i]);
                        }
                        updateField(cls, field, writeTo, stacks);
                    } else if (RecipeComponent.class.isAssignableFrom(field.getType())) {
                        updateField(cls, field, writeTo, TranslateToItemStack(o));
                    } else if (NBTTagCompound.class.isAssignableFrom(field.getType())) {
                        String json = "{}";
                        if(o instanceof String) {
                            json = o.toString();
                        } else if(o instanceof ScriptObjectMirror) {
                            json = ScriptExecutionManager.getJsonString(o);
                        }

                        try {
                            updateField(cls, field, writeTo, JsonToNBT.getTagFromJson(json));
                        } catch (NBTException e) {
                            System.out.println("Unable to convert '" + json + "' to NBT tag.");
                        }
                    } else {
                        updateField(cls, field, writeTo, ScriptUtils.convert(o, field.getType()));
                    }
                } catch (Exception e) {
                    System.err.println("Unable to deserialize '" + fieldName + "' from the provided data: " + e.getMessage());
                }
            }

            cls = cls.getSuperclass();
        } while(cls != null);
    }

    private static void updateField(Class cls, Field field, Object writeTo, Object value) throws InvocationTargetException, IllegalAccessException {
        // If we have a setter, use that...
        Method m = null;
        try {
            m = cls.getMethod(getSetterMethodName(field.getName()), field.getType());
        } catch (NoSuchMethodException e) {
            // We didn't find the method
        }

        // Otherwise, write directly to the field...
        if(m != null) {
            m.invoke(writeTo, value);
        } else {
            field.setAccessible(true);
            field.set(writeTo, value);
        }
    }

    /**
     * Translates script data to an item stack
     * @param data  The data to translate
     * @return      The ItemStack requested
     * @throws ItemMissingException When the item cannot be found in the registry.
     */
    public static RecipeComponent TranslateToItemStack(Object data) throws ItemMissingException {
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

    /**
     * Quick and dirty implementation of a field-name-to-setter-method
     * @param name  The field name
     * @return      The setter method
     */
    private static String getSetterMethodName(String name) {
        return "set" + name.substring(0,1).toUpperCase() + name.substring(1);
    }
}
