package org.winterblade.minecraft.harmony.common.drops;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.drops.IBaseDropMatcher;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;

/**
 * Created by Matt on 5/10/2016.
 */
public abstract class BaseDrop<TEvt, TMatch extends IBaseDropMatcher<TEvt>> extends BaseEventMatch<TEvt, ItemStack, TMatch> {
    private ItemStack what;
    private int min;
    private int max;

    private IEntityCallback[] onDrop;

    public ItemStack getWhat() {
        return what;
    }

    public int getMin() {
        return min < 0 ? 0 : min;
    }

    public int getMax() {
        return max < 0 ? 0 : max;
    }

    public void setWhat(ItemStack what) {
        this.what = what;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public IEntityCallback[] getOnDrop() {
        return onDrop;
    }

    public void setOnDrop(IEntityCallback[] onDrop) {
        this.onDrop = onDrop;
    }
}
