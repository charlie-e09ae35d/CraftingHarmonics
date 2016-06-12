var RecipeManager = {
    shapeless: function(output) {
        return new CraftingOperation("addShapeless", output);
    },
    shaped: function(output, width, height) {
        var op = new CraftingOperation("addShaped", output);
        if(width) op.width(width);
        if(height) op.height(height);

        return op;
    },
    remove: function(what) {
        return new RemoveOperation(what);
    },
    furnace: function(output, input, xp) {
        var op = new FurnaceOperation(output);
        if(input) op.with(input);
        if(xp) op.xp(xp);
        return op;
    }
};