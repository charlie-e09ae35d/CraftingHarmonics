package org.winterblade.minecraft.harmony.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import org.winterblade.minecraft.harmony.common.ItemUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 4/6/2016.
 */
public class FuelRegistry implements IFuelHandler {
    private static FuelRegistry instance;
    private Map<ItemStack, Integer> burnTimes;

    static {
        instance = new FuelRegistry();
    }

    private FuelRegistry() {
        burnTimes = new HashMap<>();
    }

    public void AddFuel(ItemStack fuel, int burnTime) {
        // Should deal with the previous value here...
        // Maybe check if it's less?  Or implement a priority system for config sets
        burnTimes.put(fuel, burnTime);
    }

    public static FuelRegistry getInstance() {
        return instance;
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        for (Map.Entry<ItemStack, Integer> kv : burnTimes.entrySet()) {
            if (!ItemUtility.AreItemsEquivalent(kv.getKey(), fuel)) continue;
            return kv.getValue();
        }

        return 0;
    }

    public void RemoveFuel(ItemStack itemStack) {
        burnTimes.remove(itemStack);
    }
}
