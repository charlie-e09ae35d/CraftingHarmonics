package org.winterblade.minecraft.harmony.integration.bloodmagic.matchers;

import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

import java.util.Arrays;

/**
 * Created by Matt on 7/30/2016.
 */
public abstract class BaseWillMatcher {

    private final EnumDemonWillType type;
    private final double min;
    private final double max;

    public BaseWillMatcher(WillData data) {
        this.min = data.min;
        this.max = data.max;

        // Get the first matching will type... or blow up.
        this.type = Arrays.stream(EnumDemonWillType.values()).filter(w -> w.getName().equals(data.type.toLowerCase())).findFirst().get();
    }

    protected BaseMatchResult matches(World world, BlockPos pos) {
        double currentWill = WorldDemonWillHandler.getCurrentWill(world, pos, type);
        return (min <= currentWill && currentWill <= max)
                ? BaseMatchResult.True
                : BaseMatchResult.False;
    }

    public static class WillData {
        private String type;
        private double min;
        private double max;
    }

    @Component(properties = {"bloodMagicHasDemonWill"}, dependsOn = "BloodMagic")
    @PrioritizedObject(priority = Priority.HIGH)
    public static class BlockDrop extends BaseWillMatcher implements IBlockDropMatcher {
        public BlockDrop(WillData data) {
            super(data);
        }

        /**
         * Should return true if this matcher matches the given event
         *
         * @param harvestDropsEvent The event to match
         * @param drop              The dropped item; this can be modified.
         * @return True if it should match; false otherwise
         */
        @Override
        public BaseMatchResult isMatch(BlockEvent.HarvestDropsEvent harvestDropsEvent, ItemStack drop) {
            return matches(harvestDropsEvent.getWorld(), harvestDropsEvent.getPos());
        }
    }

    @Component(properties = {"bloodMagicHasDemonWill"}, dependsOn = "BloodMagic")
    @PrioritizedObject(priority = Priority.HIGH)
    public static class MobDrop extends BaseWillMatcher implements IMobDropMatcher {
        public MobDrop(WillData data) {
            super(data);
        }

        /**
         * Should return true if this matcher matches the given event
         *
         * @param event The event to match
         * @param drop  The dropped item; this can be modified.
         * @return True if it should match; false otherwise
         */
        @Override
        public BaseMatchResult isMatch(LivingDropsEvent event, ItemStack drop) {
            return matches(event.getEntity().getEntityWorld(), event.getEntity().getPosition());
        }
    }

    @Component(properties = {"bloodMagicHasDemonWill"}, dependsOn = "BloodMagic")
    @PrioritizedObject(priority = Priority.HIGH)
    public static class MobShed extends BaseWillMatcher implements IMobShedMatcher {
        public MobShed(WillData data) {
            super(data);
        }

        /**
         * Should return true if this matcher matches the given event
         *
         * @param entityLivingBase The event to match
         * @param drop             The dropped item; this can be modified.
         * @return True if it should match; false otherwise
         */
        @Override
        public BaseMatchResult isMatch(EntityLivingBase entityLivingBase, ItemStack drop) {
            return matches(entityLivingBase.getEntityWorld(), entityLivingBase.getPosition());
        }
    }

    @Component(properties = {"bloodMagicHasDemonWill"}, dependsOn = "BloodMagic")
    @PrioritizedObject(priority = Priority.HIGH)
    public static class EntityEvent extends BaseWillMatcher implements IEntityMatcher {
        public EntityEvent(WillData data) {
            super(data);
        }

        @Override
        public BaseMatchResult isMatch(Entity entity, CallbackMetadata metadata) {
            return matches(entity.getEntityWorld(), entity.getPosition());
        }
    }

    @Component(properties = {"bloodMagicHasDemonWill"}, dependsOn = "BloodMagic")
    @PrioritizedObject(priority = Priority.HIGH)
    public static class TileEntityEvent extends BaseWillMatcher implements ITileEntityMatcher {
        public TileEntityEvent(WillData data) {
            super(data);
        }

        @Override
        public BaseMatchResult isMatch(TileEntity tileEntity, CallbackMetadata metadata) {
            return matches(tileEntity.getWorld(), tileEntity.getPos());
        }
    }
}
