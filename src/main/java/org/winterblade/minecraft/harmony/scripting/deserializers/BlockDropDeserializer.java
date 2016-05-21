package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.blocks.drops.BlockDrop;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 5/12/2016.
 */
@ScriptObjectDeserializer(deserializes = BlockDrop.class)
public class BlockDropDeserializer extends BaseDropDeserializer<BlockEvent.HarvestDropsEvent, IBlockDropMatcher, BlockDrop> {
    public BlockDropDeserializer() {super(IBlockDropMatcher.class);}

    @Override
    protected BlockDrop newInstance() {
        return new BlockDrop();
    }

    @Override
    protected void updateExtraProps(ScriptObjectMirror mirror, BlockDrop drop) {
        if(mirror.containsKey("fortuneMultiplier")) {
            drop.setFortuneMultiplier((Double) mirror.get("fortuneMultiplier"));
        }
    }
}
