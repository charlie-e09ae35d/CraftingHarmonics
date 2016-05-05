package org.winterblade.minecraft.harmony.proxies;

import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;

public class ServerProxy extends CommonProxy {
    @Override
    public void onStarted(FMLServerStartedEvent evt) {
        super.onStarted(evt);

        // This is a dummy to force ForgeHooks to be loaded, and initTools to be called to avoid an issue
        // on servers where tools aren't set until after we try to apply our ops.
        ForgeHooks.getCraftingPlayer();

        // Only do this on the server, the client will do it when they connect
        CraftingHarmonicsMod.applyBaseSets();
    }
}
