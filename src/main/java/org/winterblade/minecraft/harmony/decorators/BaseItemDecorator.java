package org.winterblade.minecraft.harmony.decorators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;
import java.util.Set;

/**
 * Created by Matt on 4/9/2016.
 */
public class BaseItemDecorator extends Item {
    private final Item decoratedItem;

    public BaseItemDecorator(Item decoratedItem) {
        this.decoratedItem = decoratedItem;
    }
    
    @Override
    public IItemPropertyGetter getPropertyGetter(ResourceLocation key) {
        return decoratedItem.getPropertyGetter(key);
    }

    @Override
    public boolean updateItemStackNBT(NBTTagCompound nbt) {
        return decoratedItem.updateItemStackNBT(nbt);
    }

    @Override
    public boolean hasCustomProperties() {
        return decoratedItem.hasCustomProperties();
    }

    @Override
    public Item setMaxStackSize(int maxStackSize) {
        return decoratedItem.setMaxStackSize(maxStackSize);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return decoratedItem.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        return decoratedItem.getStrVsBlock(stack, state);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return decoratedItem.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        return decoratedItem.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public int getMetadata(int damage) {
        return decoratedItem.getMetadata(damage);
    }

    @Override
    public boolean getHasSubtypes() {
        return decoratedItem.getHasSubtypes();
    }

    @Override
    public Item setHasSubtypes(boolean hasSubtypes) {
        return decoratedItem.setHasSubtypes(hasSubtypes);
    }

    @Override
    public int getMaxDamage() {
        return decoratedItem.getMaxDamage();
    }

    @Override
    public Item setMaxDamage(int maxDamageIn) {
        return decoratedItem.setMaxDamage(maxDamageIn);
    }

    @Override
    public boolean isDamageable() {
        return decoratedItem.isDamageable();
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return decoratedItem.hitEntity(stack, target, attacker);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {
        return decoratedItem.onBlockDestroyed(stack, worldIn, blockIn, pos, entityLiving);
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return decoratedItem.canHarvestBlock(blockIn);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        return decoratedItem.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    @Override
    public Item setFull3D() {
        return decoratedItem.setFull3D();
    }

    @Override
    public boolean isFull3D() {
        return decoratedItem.isFull3D();
    }

    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return decoratedItem.shouldRotateAroundWhenRendering();
    }

    @Override
    public Item setUnlocalizedName(String unlocalizedName) {
        return decoratedItem.setUnlocalizedName(unlocalizedName);
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return decoratedItem.getUnlocalizedNameInefficiently(stack);
    }

    @Override
    public String getUnlocalizedName() {
        return decoratedItem.getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return decoratedItem.getUnlocalizedName(stack);
    }

    @Override
    public Item setContainerItem(Item containerItem) {
        return decoratedItem.setContainerItem(containerItem);
    }

    @Override
    public boolean getShareTag() {
        return decoratedItem.getShareTag();
    }

    @Override
    public Item getContainerItem() {
        return decoratedItem.getContainerItem();
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        decoratedItem.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        decoratedItem.onCreated(stack, worldIn, playerIn);
    }

    @Override
    public boolean isMap() {
        return decoratedItem.isMap();
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return decoratedItem.getItemUseAction(stack);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return decoratedItem.getMaxItemUseDuration(stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        decoratedItem.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        decoratedItem.addInformation(stack, playerIn, tooltip, advanced);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return decoratedItem.getItemStackDisplayName(stack);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return decoratedItem.hasEffect(stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return decoratedItem.getRarity(stack);
    }

    @Override
    public boolean isItemTool(ItemStack stack) {
        return decoratedItem.isItemTool(stack);
    }

    @Override
    public int getItemEnchantability() {
        return decoratedItem.getItemEnchantability();
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        decoratedItem.getSubItems(itemIn, tab, subItems);
    }

    @Override
    public Item setCreativeTab(CreativeTabs tab) {
        return decoratedItem.setCreativeTab(tab);
    }

    @Override
    public CreativeTabs getCreativeTab() {
        return decoratedItem.getCreativeTab();
    }

    @Override
    public boolean canItemEditBlocks() {
        return decoratedItem.canItemEditBlocks();
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return decoratedItem.getIsRepairable(toRepair, repair);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return decoratedItem.getAttributeModifiers(slot, stack);
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return decoratedItem.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public boolean isRepairable() {
        return decoratedItem.isRepairable();
    }

    @Override
    public Item setNoRepair() {
        return decoratedItem.setNoRepair();
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return decoratedItem.onBlockStartBreak(itemstack, pos, player);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        decoratedItem.onUsingTick(stack, player, count);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        return decoratedItem.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return decoratedItem.getContainerItem(itemStack);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return decoratedItem.hasContainerItem(stack);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return decoratedItem.getEntityLifespan(itemStack, world);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return decoratedItem.hasCustomEntity(stack);
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return decoratedItem.createEntity(world, location, itemstack);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        return decoratedItem.onEntityItemUpdate(entityItem);
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return decoratedItem.getCreativeTabs();
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return decoratedItem.doesSneakBypassUse(stack, world, pos, player);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        decoratedItem.onArmorTick(world, player, itemStack);
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return decoratedItem.isValidArmor(stack, armorType, entity);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return decoratedItem.isBookEnchantable(stack, book);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return decoratedItem.getArmorTexture(stack, entity, slot, type);
    }

    @Override
    public FontRenderer getFontRenderer(ItemStack stack) {
        return decoratedItem.getFontRenderer(stack);
    }

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return decoratedItem.getArmorModel(entityLiving, itemStack, armorSlot, _default);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        return decoratedItem.onEntitySwing(entityLiving, stack);
    }

    @Override
    public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks) {
        decoratedItem.renderHelmetOverlay(stack, player, resolution, partialTicks);
    }

    @Override
    public int getDamage(ItemStack stack) {
        return decoratedItem.getDamage(stack);
    }

    @Override
    public int getMetadata(ItemStack stack) {
        return decoratedItem.getMetadata(stack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return decoratedItem.showDurabilityBar(stack);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return decoratedItem.getDurabilityForDisplay(stack);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return decoratedItem.getMaxDamage(stack);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return decoratedItem.isDamaged(stack);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        decoratedItem.setDamage(stack, damage);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return decoratedItem.canHarvestBlock(state, stack);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return decoratedItem.getItemStackLimit(stack);
    }

    @Override
    public void setHarvestLevel(String toolClass, int level) {
        decoratedItem.setHarvestLevel(toolClass, level);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return decoratedItem.getToolClasses(stack);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        return decoratedItem.getHarvestLevel(stack, toolClass);
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return decoratedItem.getItemEnchantability(stack);
    }

    @Override
    public boolean isBeaconPayment(ItemStack stack) {
        return decoratedItem.isBeaconPayment(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return decoratedItem.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return decoratedItem.initCapabilities(stack, nbt);
    }

    @Override
    public ImmutableMap<String, ITimeValue> getAnimationParameters(ItemStack stack, World world, EntityLivingBase entity) {
        return decoratedItem.getAnimationParameters(stack, world, entity);
    }

    @Override
    public int hashCode() {
        return decoratedItem.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return decoratedItem.equals(obj);
    }

    @Override
    public String toString() {
        return decoratedItem.toString() + "+" + this.getClass().getSimpleName();
    }
}
