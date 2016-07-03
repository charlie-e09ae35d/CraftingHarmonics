package org.winterblade.minecraft.harmony.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.world.sky.ClientSkyModifications;

import javax.annotation.Nullable;
import java.util.HashSet;

/**
 * Created by Matt on 5/31/2016.
 */
public class ProxiedWorldProvider extends WorldProvider {
    private final WorldProvider wrapped;

    public ProxiedWorldProvider(WorldProvider wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Replaces the given world provider with our own.
     * @param world    The world to proxy
     */
    public static void injectProvider(World world) {
        // DO NOT PROXY THE WORLD ON THE SERVER AT ALL:
        if(!world.isRemote) return;

        ProxiedWorldProvider provider = new ProxiedWorldProvider(world.provider);
        ObfuscationReflectionHelper.setPrivateValue(World.class, world, provider, "field_73011_w");
    }

    @Override
    public DimensionType getDimensionType() {
        return wrapped.getDimensionType();
    }

    /**
     * Creates the light to brightness table
     */
    @Override
    protected void generateLightBrightnessTable() {
        // Doesn't matter.
    }

    /**
     * creates a new world chunk manager for WorldProvider
     */
    @Override
    protected void createBiomeProvider() {
        // Also doesn't matter.
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return wrapped.createChunkGenerator();
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     *
     * @param x
     * @param z
     */
    @Override
    public boolean canCoordinateBeSpawn(int x, int z) {
        return wrapped.canCoordinateBeSpawn(x, z);
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     *
     * @param worldTime
     * @param partialTicks
     */
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return wrapped.calculateCelestialAngle(worldTime, partialTicks);
    }

    @Override
    public int getMoonPhase(long worldTime) {
        return wrapped.getMoonPhase(worldTime);
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    @Override
    public boolean isSurfaceWorld() {
        return wrapped.isSurfaceWorld();
    }

    /**
     * Returns array with sunrise/sunset colors
     *
     * @param celestialAngle
     * @param partialTicks
     */
    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        return wrapped.calcSunriseSunsetColors(celestialAngle, partialTicks);
    }

    /**
     * Return Vec3D with biome specific fog color
     *
     * @param x
     * @param z
     */
    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float x, float z) {
        return wrapped.getFogColor(x, z);
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    @Override
    public boolean canRespawnHere() {
        return wrapped.canRespawnHere();
    }

    /**
     * the y level at which clouds are rendered.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return wrapped.getCloudHeight();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return wrapped.isSkyColored();
    }

    @Override
    public BlockPos getSpawnCoordinate() {
        return wrapped.getSpawnCoordinate();
    }

    @Override
    public int getAverageGroundLevel() {
        return wrapped.getAverageGroundLevel();
    }

    /**
     * Returns a double value representing the Y value relative to the top of the map at which void fog is at its
     * maximum. The default factor of 0.03125 relative to 256, for example, means the void fog will be at its maximum at
     * (256*0.03125), or 8.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public double getVoidFogYFactor() {
        return wrapped.getVoidFogYFactor();
    }

    /**
     * Returns true if the given X,Z coordinate should show environmental fog.
     *
     * @param x
     * @param z
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z) {
        return wrapped.doesXZShowFog(x, z);
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return wrapped.getBiomeProvider();
    }

    @Override
    public boolean doesWaterVaporize() {
        return wrapped.doesWaterVaporize();
    }

    @Override
    public boolean getHasNoSky() {
        return wrapped.getHasNoSky();
    }

    @Override
    public float[] getLightBrightnessTable() {
        return wrapped.getLightBrightnessTable();
    }

    @Override
    public WorldBorder createWorldBorder() {
        return wrapped.createWorldBorder();
    }

    /**
     * Sets the providers current dimension ID, used in default getSaveFolder()
     * Added to allow default providers to be registered for multiple dimensions.
     * This is to denote the exact dimension ID opposed to the 'type' in WorldType
     *
     * @param dim Dimension ID
     */
    @Override
    public void setDimension(int dim) {
        wrapped.setDimension(dim);
    }

    @Override
    public int getDimension() {
        return wrapped.getDimension();
    }

    /**
     * Returns the sub-folder of the world folder that this WorldProvider saves to.
     * EXA: DIM1, DIM-1
     *
     * @return The sub-folder name to save this world's chunks to.
     */
    @Override
    public String getSaveFolder() {
        return wrapped.getSaveFolder();
    }

    /**
     * A message to display to the user when they transfer to this dimension.
     *
     * @return The message to be displayed
     */
    @Override
    public String getWelcomeMessage() {
        return wrapped.getWelcomeMessage();
    }

    /**
     * A Message to display to the user when they transfer out of this dismension.
     *
     * @return The message to be displayed
     */
    @Override
    public String getDepartMessage() {
        return wrapped.getDepartMessage();
    }

    /**
     * The dimensions movement factor. Relative to normal overworld.
     * It is applied to the players position when they transfer dimensions.
     * Exa: Nether movement is 8.0
     *
     * @return The movement factor
     */
    @Override
    public double getMovementFactor() {
        return wrapped.getMovementFactor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderHandler getSkyRenderer() {
        return wrapped.getSkyRenderer();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setSkyRenderer(IRenderHandler skyRenderer) {
        wrapped.setSkyRenderer(skyRenderer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderHandler getCloudRenderer() {
        return wrapped.getCloudRenderer();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setCloudRenderer(IRenderHandler renderer) {
        wrapped.setCloudRenderer(renderer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderHandler getWeatherRenderer() {
        return wrapped.getWeatherRenderer();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setWeatherRenderer(IRenderHandler renderer) {
        wrapped.setWeatherRenderer(renderer);
    }

    @Override
    public BlockPos getRandomizedSpawnPoint() {
        return wrapped.getRandomizedSpawnPoint();
    }

    /**
     * Determine if the cursor on the map should 'spin' when rendered, like it does for the player in the nether.
     *
     * @param entity The entity holding the map, playername, or frame-ENTITYID
     * @param x      X Position
     * @param y      Y Position
     * @param z      Z Position
     * @return True to 'spin' the cursor
     */
    @Override
    public boolean shouldMapSpin(String entity, double x, double y, double z) {
        return wrapped.shouldMapSpin(entity, x, y, z);
    }

    /**
     * Determines the dimension the player will be respawned in, typically this brings them back to the overworld.
     *
     * @param player The player that is respawning
     * @return The dimension to respawn the player in
     */
    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        return wrapped.getRespawnDimension(player);
    }

    @Override
    public Biome getBiomeForCoords(BlockPos pos) {
        return wrapped.getBiomeForCoords(pos);
    }

    @Override
    public boolean isDaytime() {
        return wrapped.isDaytime();
    }

    /**
     * The current sun brightness factor for this dimension.
     * 0.0f means no light at all, and 1.0f means maximum sunlight.
     * This will be used for the "calculateSkylightSubtracted"
     * which is for Sky light value calculation.
     *
     * @param par1
     * @return The current brightness factor
     */
    @Override
    public float getSunBrightnessFactor(float par1) {
        return wrapped.getSunBrightnessFactor(par1);
    }

    /**
     * Calculates the current moon phase factor.
     * This factor is effective for slimes.
     * (This method do not affect the moon rendering)
     */
    @Override
    public float getCurrentMoonPhaseFactor() {
        return wrapped.getCurrentMoonPhaseFactor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        // Prevent a null camera entity from being tested; this is occurring due to someone's mod...
        if(cameraEntity == null) {
            LogHelper.warn("Unable to get the sky color for world {} because a null entity was passed in.", wrapped.getDimension());
            return new Vec3d(0,0,0);
        }

        try {
            return ClientSkyModifications.getSkyColorFor(cameraEntity, wrapped.getSkyColor(cameraEntity, partialTicks), wrapped.getDimension());
        } catch(Exception e) {
            LogHelper.warn("Error getting sky color for world {}", wrapped.getDimension(), e);
            return new Vec3d(0,0,0);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getCloudColor(float partialTicks) {
        return wrapped.getCloudColor(partialTicks);
    }

    /**
     * Gets the Sun Brightness for rendering sky.
     *
     * @param par1
     */
    @Override
    @SideOnly(Side.CLIENT)
    public float getSunBrightness(float par1) {
        return wrapped.getSunBrightness(par1);
    }

    /**
     * Gets the Star Brightness for rendering sky.
     *
     * @param par1
     */
    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1) {
        return wrapped.getStarBrightness(par1);
    }

    @Override
    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful) {
        wrapped.setAllowedSpawnTypes(allowHostile, allowPeaceful);
    }

    @Override
    public void calculateInitialWeather() {
        wrapped.calculateInitialWeather();
    }

    @Override
    public void updateWeather() {
        wrapped.updateWeather();
    }

    @Override
    public boolean canBlockFreeze(BlockPos pos, boolean byWater) {
        return wrapped.canBlockFreeze(pos, byWater);
    }

    @Override
    public boolean canSnowAt(BlockPos pos, boolean checkLight) {
        return wrapped.canSnowAt(pos, checkLight);
    }

    @Override
    public void setWorldTime(long time) {
        wrapped.setWorldTime(time);
    }

    @Override
    public long getSeed() {
        return wrapped.getSeed();
    }

    @Override
    public long getWorldTime() {
        return wrapped.getWorldTime();
    }

    @Override
    public BlockPos getSpawnPoint() {
        return wrapped.getSpawnPoint();
    }

    @Override
    public void setSpawnPoint(BlockPos pos) {
        wrapped.setSpawnPoint(pos);
    }

    @Override
    public boolean canMineBlock(EntityPlayer player, BlockPos pos) {
        return wrapped.canMineBlock(player, pos);
    }

    @Override
    public boolean isBlockHighHumidity(BlockPos pos) {
        return wrapped.isBlockHighHumidity(pos);
    }

    @Override
    public int getHeight() {
        return wrapped.getHeight();
    }

    @Override
    public int getActualHeight() {
        return wrapped.getActualHeight();
    }

    @Override
    public double getHorizon() {
        return wrapped.getHorizon();
    }

    @Override
    public void resetRainAndThunder() {
        wrapped.resetRainAndThunder();
    }

    @Override
    public boolean canDoLightning(Chunk chunk) {
        return wrapped.canDoLightning(chunk);
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        return wrapped.canDoRainSnowIce(chunk);
    }

    /**
     * Called when a Player is added to the provider's world.
     *
     * @param player
     */
    @Override
    public void onPlayerAdded(EntityPlayerMP player) {
        wrapped.onPlayerAdded(player);
    }

    /**
     * Called when a Player is removed from the provider's world.
     *
     * @param player
     */
    @Override
    public void onPlayerRemoved(EntityPlayerMP player) {
        wrapped.onPlayerRemoved(player);
    }

    /**
     * Called when the world is performing a save. Only used to save the state of the Dragon Boss fight in
     * WorldProviderEnd in Vanilla.
     */
    @Override
    public void onWorldSave() {
        wrapped.onWorldSave();
    }

    /**
     * Called when the world is updating entities. Only used in WorldProviderEnd to update the DragonFightManager in
     * Vanilla.
     */
    @Override
    public void onWorldUpdateEntities() {
        wrapped.onWorldUpdateEntities();
    }

    /**
     * Called to determine if the chunk at the given chunk coordinates within the provider's world can be dropped. Used
     * in WorldProviderSurface to prevent spawn chunks from being unloaded.
     *
     * @param x
     * @param z
     */
    @Override
    public boolean canDropChunk(int x, int z) {
        return wrapped.canDropChunk(x, z);
    }

}
