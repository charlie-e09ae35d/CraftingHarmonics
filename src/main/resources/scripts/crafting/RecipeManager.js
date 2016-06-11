var RecipeManager = {
    shapeless: function(output) {
        return new CraftingOperation("addShapeless", output);
    },
    shaped: function(output, width, height) {
        var op = new CraftingOperation("addShaped", output);
        if(width) op.width(width);
        if(height) op.height(height);

        return op;
    }
};