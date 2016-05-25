package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseHeldEquipmentMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"mobHasMainhand", "mobMainhandNbt", "mobMainhandFuzzyNbt"})
@PrioritizedObject(priority = Priority.HIGH)
public class MobHasMainhandMatcher extends BaseHeldEquipmentMatcher implements IEntityMatcher {
    private final static ItemStack dummy = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("minecraft:cobblestone")), 1);

    public MobHasMainhandMatcher(ItemStack item) {
        this(item, null);
    }

    public MobHasMainhandMatcher(ItemStack item, NBTTagCompound nbt) {
        this(item, nbt, false);
    }

    public MobHasMainhandMatcher(ItemStack item, NBTTagCompound nbt, boolean isFuzzy) {
        super(item, 0, false, nbt, isFuzzy, EnumHand.MAIN_HAND);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity The event to match
     * @param metadata Event metadata.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entity, IEntityMatcherData metadata) {
        dummy.stackSize = 1;
        return matches(entity, dummy);
    }
}