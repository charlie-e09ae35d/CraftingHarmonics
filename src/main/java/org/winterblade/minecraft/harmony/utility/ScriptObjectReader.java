package org.winterblade.minecraft.harmony.utility;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

        for(String key : data.keySet()) {
            try {
                updateField(cls, key, writeTo, data.get(key));
            } catch (Exception e) {
                System.err.println("Unable to deserialize '" + key + "' from the provided data: " + e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T convertData(Object input, Class<T> cls) {
        if (cls.isAssignableFrom(OreDictionaryItemStack[].class)) {
            Object[] items = (Object[]) ScriptUtils.convert(input, Object[].class);
            OreDictionaryItemStack[] stacks = new OreDictionaryItemStack[items.length];

            for (int i = 0; i < items.length; i++) {
                try {
                    stacks[i] = TranslateToOreDictionaryItemStack((String)items[i]);
                } catch (ItemMissingException e) {
                    stacks[i] = null;
                }
            }

            return (T) stacks;
        }

        if(cls.isAssignableFrom(OreDictionaryItemStack.class)) {
            try {
                return (T) TranslateToOreDictionaryItemStack((String)input);
            } catch (ItemMissingException e) {
                return null;
            }
        }

        if (cls.isAssignableFrom(RecipeComponent[].class)) {
            Object[] items = (Object[])ScriptUtils.convert(input, Object[].class);
            RecipeComponent[] stacks = new RecipeComponent[items.length];
            for (int i = 0; i < items.length; i++) {
                try {
                    stacks[i] = TranslateToItemStack(items[i]);
                } catch (ItemMissingException e) {
                    stacks[i] = null;
                }
            }

            return (T) stacks;
        }

        if (cls.isAssignableFrom(RecipeComponent.class)) {
            try {
                return (T) TranslateToItemStack(input);
            } catch (ItemMissingException e) {
                return null;
            }
        }

        if (cls.isAssignableFrom(NBTTagCompound.class)) {
            String json = "{}";
            if(input instanceof String) {
                json = input.toString();
            } else if(input instanceof ScriptObjectMirror) {
                json = ScriptExecutionManager.getJsonString(input);
            }

            try {
                return (T) JsonToNBT.getTagFromJson(json);
            } catch (NBTException e) {
                System.out.println("Unable to convert '" + json + "' to NBT tag.");
            }
        }

        if (cls.isAssignableFrom(ItemStack.class)) {
            try {
                return (T) ItemRegistry.TranslateToItemStack((String)input);
            } catch (ItemMissingException e) {
                return null;
            }
        }

        if (cls.isAssignableFrom(Item.class)) {
            try {
                return (T) ItemRegistry.TranslateToItemStack((String)input).getItem();
            } catch (ItemMissingException e) {
                return null;
            }
        }

        return (T) ScriptUtils.convert(input, cls);
    }

    private static void updateField(Class cls, String field, Object writeTo, Object value) throws InvocationTargetException, IllegalAccessException {
        // If we have a setter, use that...
        Method m = getFirstMethodByName(cls, field);

        if(m != null) {
            // Convert and call
            Class c = m.getParameterTypes()[0];
            m.invoke(writeTo, convertData(value, c));
            return;
        }

        Field f = getFieldByName(cls, field);

        if(f == null) return;

        f.setAccessible(true);
        f.set(writeTo, convertData(value, f.getType()));
    }

    private static Method getFirstMethodByName(Class cls, String name) {
        Method[] methods = cls.getMethods();
        name = name.toLowerCase();

        for(Method method : methods) {
            if (method.getName().toLowerCase().equals("set" + name) && method.getParameterCount() == 1) return method;
        }

        return null;
    }

    private static Field getFieldByName(Class cls, String name) {
        Field field = null;

        Class cur = cls;

        do {
            try {
                field = cur.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                // Ascend.
            }
            cur = cur.getSuperclass();
        } while(field == null && cur != null);

        if(field == null) return null;

        int modifiers = field.getModifiers();

        // Don't bother deserializing in these cases:
        return Modifier.isFinal(modifiers)
                || Modifier.isTransient(modifiers)
                || Modifier.isAbstract(modifiers) ? null : field;

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
}
