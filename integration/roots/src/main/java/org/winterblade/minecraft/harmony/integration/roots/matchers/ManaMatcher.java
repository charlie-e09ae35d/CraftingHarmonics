package org.winterblade.minecraft.harmony.integration.roots.matchers;

import elucent.roots.capability.mana.ManaProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

/**
 * Created by Matt on 7/30/2016.
 */
public abstract class ManaMatcher {
    private final int min;
    private final int max;

    public ManaMatcher(ManaData data) {
        min = data.min;
        max = data.max;
    }

    public BaseMatchResult matches(Entity entity) {
        if(entity == null
                || !EntityPlayer.class.isAssignableFrom(entity.getClass())
                || !entity.hasCapability(ManaProvider.manaCapability, null)) return BaseMatchResult.False;

        float mana = ManaProvider.get((EntityPlayer) entity).getMana();

        return (min <= mana && mana <= max)
                ? BaseMatchResult.True
                : BaseMatchResult.False;
    }

    public static class ManaData {
        private int min;
        private int max;
    }


    @Component(properties = {"rootsHasMana"}, dependsOn = "roots")
    @PrioritizedObject(priority = Priority.HIGH)
    public static class BlockDrop extends ManaMatcher implements IBlockDropMatcher {
        public BlockDrop(ManaData data) {
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
            return matches(harvestDropsEvent.getHarvester());
        }
    }

    @Component(properties = {"rootsHasMana"}, dependsOn = "roots")
    @PrioritizedObject(priority = Priority.HIGH)
    public static class MobDrop extends ManaMatcher implements IMobDropMatcher {
        public MobDrop(ManaData data) {
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
            if(event.getSource() == null) return BaseMatchResult.False;
            return matches(event.getSource().getEntity());
        }
    }

    @Component(properties = {"rootsHasMana"}, dependsOn = "roots")
    @PrioritizedObject(priority = Priority.HIGH)
    public static class MobShed extends ManaMatcher implements IMobShedMatcher {
        public MobShed(ManaData data) {
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
            return matches(entityLivingBase);
        }
    }

    @Component(properties = {"rootsHasMana"}, dependsOn = "roots")
    @PrioritizedObject(priority = Priority.HIGH)
    public static class EntityEvent extends ManaMatcher implements IEntityMatcher {
        public EntityEvent(ManaData data) {
            super(data);
        }

        @Override
        public BaseMatchResult isMatch(Entity entity, CallbackMetadata metadata) {
            return matches(entity);
        }
    }
}
