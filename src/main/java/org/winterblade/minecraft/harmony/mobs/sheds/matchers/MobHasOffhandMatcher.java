package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseHeldEquipmentMatcher;

/**
 * Created by Matt on 5/15/2016.
 */
@Component(properties = {"mobHasOffhand", "mobOffhandNbt", "mobOffhandFuzzyNbt"})
@PrioritizedObject(priority = Priority.HIGH)
public class MobHasOffhandMatcher extends BaseHeldEquipmentMatcher implements IMobShedMatcher {
    public MobHasOffhandMatcher(ItemStack item) {
        this(item, null);
    }

    public MobHasOffhandMatcher(ItemStack item, NBTTagCompound nbt) {
        this(item, nbt, false);
    }

    public MobHasOffhandMatcher(ItemStack item, NBTTagCompound nbt, boolean isFuzzy) {
        super(item, 0, false, nbt, isFuzzy, EnumHand.OFF_HAND);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entity, ItemStack drop) {
        return matches(entity, drop);
    }
}
