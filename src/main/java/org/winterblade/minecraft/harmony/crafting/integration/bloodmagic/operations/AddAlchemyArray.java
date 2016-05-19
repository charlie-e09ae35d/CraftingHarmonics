package org.winterblade.minecraft.harmony.crafting.integration.bloodmagic.operations;

import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBinding;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffectCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.crafting.integration.bloodmagic.ReflectedBloodMagicRegistry;
import org.winterblade.minecraft.harmony.api.utility.LogHelper;

/**
 * Created by Matt on 4/22/2016.
 */
@RecipeOperation(name = "addAlchemyArray", dependsOn = "BloodMagic")
public class AddAlchemyArray extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private ItemStack output;
    private RecipeInput with;
    private String circle;
    private ItemStack catalyst;
    private boolean isBinding;
    private int time;

    /*
     * Computed properties
     */
    private transient ResourceLocation renderer;
    private transient AlchemyArrayEffect effect;
    private transient Object input;

    @Override
    public void Init() throws ItemMissingException {
        // Figure out our renderer first...
        renderer = circle != null && !circle.equals("")
                ? new ResourceLocation(circle)
                : null;

        // Our effect...
        effect = isBinding
                ? new AlchemyArrayEffectBinding(output)
                : new AlchemyArrayEffectCrafting(output, time <= 0 ? 300 : time);

        // Finally, our input...
        input = with.getFacsimileItem();
    }

    @Override
    public void Apply() {
        LogHelper.info("Adding Alchemy Array recipe for '" + ItemRegistry.outputItemName(output) + "'.");
        if(input instanceof String) {
            ReflectedBloodMagicRegistry.addAlchemyArray((String)input, catalyst, effect, renderer);
        } else if(input instanceof ItemStack) {
            ReflectedBloodMagicRegistry.addAlchemyArray((ItemStack)input, catalyst, effect, renderer);
        }
    }

    @Override
    public void Undo() {
        if(input instanceof String) {
            ReflectedBloodMagicRegistry.removeAlchemyArray((String)input, catalyst);
        } else if(input instanceof ItemStack) {
            ReflectedBloodMagicRegistry.removeAlchemyArray((ItemStack)input, catalyst);
        }
    }
}
