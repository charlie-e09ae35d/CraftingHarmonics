package org.winterblade.minecraft.harmony.crafting.transformers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;

/**
 * Created by Matt on 4/10/2016.
 */
public class DamageOnCraft implements IItemStackTransformer {
    private final int by;

    public DamageOnCraft(int by) {
        this.by = by;
    }

    @Override
    public ItemStack transform(ItemStack input, EntityPlayer craftingPlayer) {
        if(!input.isItemStackDamageable()) return input;

        // Shockingly, there's already code to prevent us from going below 0 (undamaged)
        int newDamage = input.getItemDamage()+by;
        input.setItemDamage(newDamage);

        // Check if we've destroyed it.
        if (input.getMetadata() > input.getMaxDamage())
        {
            ForgeEventFactory.onPlayerDestroyItem(craftingPlayer, input, null);
            return null;
        }
        return input;
    }
}
