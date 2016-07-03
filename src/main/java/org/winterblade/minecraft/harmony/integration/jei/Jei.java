package org.winterblade.minecraft.harmony.integration.jei;

import io.netty.util.internal.ConcurrentSet;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Created by Matt on 4/15/2016.
 */
@JEIPlugin
public class Jei implements IModPlugin {
    private static final Set<ItemStack> hiddenItems = new ConcurrentSet<>();
    private static IModRegistry jeiRegistry;

    /**
     * Register this mod plugin with the mod registry.
     * Called just before the game launches.
     * Will be called again if config
     *
     * @param registry
     */
    @Override
    public void register(@Nonnull IModRegistry registry) {
        jeiRegistry = registry;

        for (ItemStack stack : hiddenItems) {
            jeiRegistry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(stack);
        }
    }

    /**
     * Called when jei's runtime features are available, after all mods have registered.
     *
     * @param jeiRuntime
     * @since JEI 2.23.0
     */
    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {

    }

    /**
     * Reloads JEI, if the user has it enabled.
     */
    public static void reloadJEI() {
        // If we don't have JEI, we don't care...
        if(jeiRegistry == null) return;
        jeiRegistry.getJeiHelpers().reload();
    }

    /**
     * Removes the given item stack from the blacklist
     * @param itemStack    The item to show
     */
    public static void show(ItemStack itemStack) {
        hiddenItems.remove(itemStack);

        // If we don't have JEI, we don't care...
        if(jeiRegistry == null) return;

        jeiRegistry.getJeiHelpers().getItemBlacklist().removeItemFromBlacklist(itemStack);
    }

    /**
     * Adds the given item stack to the blacklist
     * @param itemStack    The item to hide
     */
    public static void hide(ItemStack itemStack) {
        hiddenItems.add(itemStack);

        // If we don't have JEI, we don't care...
        if(jeiRegistry == null) return;

        jeiRegistry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(itemStack);
    }
}
