package org.winterblade.minecraft.harmony.crafting.transformers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.events.ItemOnCraftedCallback;
import org.winterblade.minecraft.harmony.crafting.events.ItemOnCraftedEvent;
import org.winterblade.minecraft.harmony.crafting.events.wrappers.ItemStackWrapper;

import javax.annotation.Nonnull;
import javax.script.Invocable;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

/**
 * Created by Matt on 4/13/2016.
 */
@Component(properties = {"onCraft"})
public class FunctionCallbackOnCraft implements IItemStackTransformer {
    private final ItemOnCraftedCallback fn;

    public FunctionCallbackOnCraft(ItemOnCraftedCallback fn) throws ItemMissingException {
        this.fn = fn;
    }

    @Override
    public ItemStack transform(ItemStack input, EntityPlayer craftingPlayer) {
        ItemStackWrapper wrapper = new ItemStackWrapper(input);
        try {
            fn.apply(new ItemOnCraftedEvent(wrapper));
        } catch(Exception ex) {
            System.err.println("Error with onCraft callback function: " + ex.getMessage());
        }

        // Return the now modified input:
        return wrapper.getItemStack();
    }

    @Nonnull
    @Override
    public IItemStackTransformer[] getImpliedTransformers() {
        return new IItemStackTransformer[0];
    }
}


