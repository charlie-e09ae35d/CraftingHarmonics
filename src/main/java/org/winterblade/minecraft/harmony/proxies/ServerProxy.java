package org.winterblade.minecraft.harmony.proxies;

import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;

public class ServerProxy extends CommonProxy {
    @Override
    public void onStarted(FMLServerStartedEvent evt) {
        super.onStarted(evt);

        // Only do this on the server, the client will do it when they connect
        CraftingHarmonicsMod.initSets();
        CraftingHarmonicsMod.applySets(new String[]{"default"});
    }
}