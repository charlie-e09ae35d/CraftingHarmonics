package org.winterblade.minecraft.harmony.integration.bloodmagic.operations;

import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectBinding;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffectCrafting;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyCircleRenderer;
import WayofTime.bloodmagic.client.render.alchemyArray.BindingAlchemyCircleRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.integration.bloodmagic.ReflectedBloodMagicRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 4/22/2016.
 */
@Operation(name = "addAlchemyArray", dependsOn = "BloodMagic")
public class AddAlchemyArray extends BasicOperation {
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
    private transient AlchemyCircleRenderer renderer;
    private transient AlchemyArrayEffect effect;
    private transient Object input;

    @Override
    public void init() throws OperationException {
        // Figure out our renderer first...
        renderer = isBinding
                ? new BindingAlchemyCircleRenderer()
                : circle != null && !circle.equals("")
                    ? new AlchemyCircleRenderer(new ResourceLocation(circle))
                    : null;

        // Our effect...
        effect = isBinding
                ? new AlchemyArrayEffectBinding(output)
                : new AlchemyArrayEffectCrafting(output, time <= 0 ? 300 : time);

        // Finally, our input...
        input = with.getFacsimileItem();
    }

    @Override
    public void apply() {
        LogHelper.info("Adding Alchemy Array recipe for '" + ItemUtility.outputItemName(output) + "'.");
        if(input instanceof String) {
            ReflectedBloodMagicRegistry.addAlchemyArray((String)input, catalyst, effect, renderer);
        } else if(input instanceof ItemStack) {
            ReflectedBloodMagicRegistry.addAlchemyArray((ItemStack)input, catalyst, effect, renderer);
        }
    }

    @Override
    public void undo() {
        if(input instanceof String) {
            ReflectedBloodMagicRegistry.removeAlchemyArray((String)input, catalyst);
        } else if(input instanceof ItemStack) {
            ReflectedBloodMagicRegistry.removeAlchemyArray((ItemStack)input, catalyst);
        }
    }
}
