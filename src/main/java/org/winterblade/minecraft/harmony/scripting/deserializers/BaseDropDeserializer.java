package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.drops.IBaseDropMatcher;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.blocks.BlockMatcher;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.common.drops.BaseDrop;
import org.winterblade.minecraft.harmony.entities.callbacks.BaseEntityCallback;
import org.winterblade.minecraft.harmony.scripting.DeserializerHelpers;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;

/**
 * Created by Matt on 5/12/2016.
 */
abstract class BaseDropDeserializer <TEvt, TMatcher extends IBaseDropMatcher<TEvt>, T extends BaseDrop<TEvt, TMatcher>>
        extends BaseMatchingDeserializer<TEvt, ItemStack, TMatcher, T>
        implements IScriptObjectDeserializer {

    private static final IScriptObjectDeserializer CALLBACK_DESERIALIZER = new BaseEntityCallback.Deserializer();

    BaseDropDeserializer(Class<TMatcher> matcherClass) {
        super(matcherClass);
    }

    protected abstract T newInstance(String type);

    protected void updateExtraProps(ScriptObjectMirror mirror, T output) {
        // If no updates are required, don't make them override it.
    }

    @Override
    protected void update(ScriptObjectMirror mirror, T output) {
        // If have a drop...
        if (mirror.containsKey("what")) {
            try {
                output.setWhat(ItemUtility.translateToItemStack(mirror.get("what").toString()));
            } catch (OperationException e) {
                LogHelper.error("Couldn't convert '" + mirror.get("what") + "' to a valid item string.");
                return;
            }
        }

        if(mirror.containsKey("min")) {
            output.setMin((Integer) mirror.get("min"));
        }

        if(mirror.containsKey("max")) {
            output.setMax((Integer) mirror.get("max"));
        }

        if(mirror.containsKey("onDrop")) {
            output.setOnDrop(DeserializerHelpers
                    .convertArrayWithDeserializer(mirror, "onDrop", CALLBACK_DESERIALIZER, IEntityCallback.class));
        }

        // Allow the actual deserializer to do its work:
        updateExtraProps(mirror, output);
    }
}

