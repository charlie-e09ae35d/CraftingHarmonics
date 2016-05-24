package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/11/2016.
 */
public class BaseBiomeTypeMatcher {
    private final List<BiomeDictionary.Type> types = new ArrayList<>();

    public BaseBiomeTypeMatcher(String[] biomesTags) {
        // Add all our tags:
        for(String tag : biomesTags) {
            tag = tag.toUpperCase();
            for(BiomeDictionary.Type type : BiomeDictionary.Type.values()) {
                if(!type.name().equals(tag)) continue;
                types.add(type);
                break;
            }
        }
    }

    protected BaseMatchResult matches(Entity entity) {
        return matches(entity.getEntityWorld(), entity.getPosition());
    }

    protected BaseMatchResult matches(World world, BlockPos pos) {
        if(world == null) return BaseMatchResult.False;

        // Get all the tags for this biome:
        BiomeDictionary.Type[] biomeTags = BiomeDictionary.getTypesForBiome(
                world.getBiomeGenForCoords(pos));

        // Figure out if we match:
        for(BiomeDictionary.Type type : biomeTags) {
            if(!types.contains(type)) continue;
            return BaseMatchResult.True;
        }

        return BaseMatchResult.False;
    }

}
