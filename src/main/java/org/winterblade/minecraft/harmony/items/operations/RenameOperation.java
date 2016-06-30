package org.winterblade.minecraft.harmony.items.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.LanguageMap;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Matt on 6/30/2016.
 */
@Operation(name = "rename")
public class RenameOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;
    private String name;

    /*
     * Computed properties
     */
    private transient String oldName;
    private transient InputStream newNameStream;
    private transient InputStream oldNameStream;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(what == null || name == null) throw new OperationException("Invalid 'rename' operation.");
        oldName = what.getDisplayName();
        String translationBase = what.getUnlocalizedName() + ".name";

        newNameStream = new ByteArrayInputStream((translationBase +"="+name).getBytes(StandardCharsets.UTF_8));
        oldNameStream = new ByteArrayInputStream((translationBase +"="+oldName).getBytes(StandardCharsets.UTF_8));

        // Allow us to get back here each time:
        newNameStream.mark(0);
        oldNameStream.mark(0);
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        try {
            newNameStream.reset();
        } catch (IOException e) {
            LogHelper.warn("Unable to reset new name stream when renaming {}", ItemUtility.outputItemName(what));
        }
        LogHelper.info("Updating {} to be named {} instead of {}.", ItemUtility.outputItemName(what), name, oldName);
        LanguageMap.inject(newNameStream);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        try {
            oldNameStream.reset();
        } catch (IOException e) {
            LogHelper.warn("Unable to reset old name stream when renaming {}", ItemUtility.outputItemName(what));
        }
        LanguageMap.inject(oldNameStream);
    }
}
