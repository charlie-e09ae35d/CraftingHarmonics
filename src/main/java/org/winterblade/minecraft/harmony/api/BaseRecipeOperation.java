package org.winterblade.minecraft.harmony.api;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.utility.OreDictionaryItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by Matt on 4/6/2016.
 */
public abstract class BaseRecipeOperation implements IRecipeOperation {
    private int priority;

    @Override
    public int compareTo(IRecipeOperation o) {
        // Don't try and sort:
        if(!(BaseRecipeOperation.class.isAssignableFrom(o.getClass()))) return 0;

        return priority - ((BaseRecipeOperation) o).priority;
    }

    /**
     * Populates the operation out of the data provided.
     * @param data  The operation data.
     * @return      If the operation succeeded.
     */
    public final boolean Convert(ScriptObjectMirror data) {
        try {
            ReadData(data);
            return true;
        }
        catch(Exception e) {
            System.out.println("Error creating " + getClass().getSimpleName());
            return false;
        }
    }

    /**
     * Used to convert the provided operation from the file into the given recipe.
     * @param data The operation data
     */
    protected void ReadData(ScriptObjectMirror data) throws ItemMissingException {
        // Base implementation just attempts to map properties one-to-one
        Class cls = getClass();

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
                        Object[] items = (Object[])ScriptUtils.convert(o, Object[].class);
                        OreDictionaryItemStack[] stacks = new OreDictionaryItemStack[items.length];
                        for (int i = 0; i < items.length; i++) {
                            stacks[i] = ItemRegistry.TranslateToOreDictionaryItemStack(items[i]);
                        }
                        field.set(this, stacks);
                    } else if(OreDictionaryItemStack.class.isAssignableFrom(field.getType())) {
                        field.set(this, ItemRegistry.TranslateToOreDictionaryItemStack(o));
                    } else if (ItemStack[].class.isAssignableFrom(field.getType())) {
                        Object[] items = (Object[])ScriptUtils.convert(o, Object[].class);
                        ItemStack[] stacks = new ItemStack[items.length];
                        for (int i = 0; i < items.length; i++) {
                            stacks[i] = ItemRegistry.TranslateToItemStack(items[i]);
                        }
                        field.set(this, stacks);
                    } else if (ItemStack.class.isAssignableFrom(field.getType())) {
                        field.set(this, ItemRegistry.TranslateToItemStack(o));
                    } else if (NBTTagCompound.class.isAssignableFrom(field.getType())) {
                        String json = "{}";
                        if(o instanceof String) {
                            json = o.toString();
                        } else if(o instanceof ScriptObjectMirror) {
                            json = data.callMember("getJson", o).toString();
                        }

                        try {
                            field.set(this, JsonToNBT.getTagFromJson(json));
                        } catch (NBTException e) {
                            System.out.println("Unable to convert '" + json + "' to NBT tag.");
                        }
                    } else {
                        field.set(this, ScriptUtils.convert(o, field.getType()));
                    }
                } catch (Exception e) {
                    System.err.println("Unable to deserialize '" + fieldName + "' from the provided data: " + e.getMessage());
                }
            }

            cls = cls.getSuperclass();
        } while(cls != null);
    };

    /**
     * Reads a parameter from the data, if it's the correct type.
     * @param data  The data
     * @param name  The name to read in
     * @param <T>   The type to read in
     * @return      The data, or null if it's not found.
     */
    protected <T> T Read(ScriptObjectMirror data, String name, Class<T> clazz) {
        if(!data.hasMember(name)) return null;

        Object o = data.get(name);
        if(!clazz.isAssignableFrom(o.getClass())) return null;
        return clazz.cast(o);
    }

}
