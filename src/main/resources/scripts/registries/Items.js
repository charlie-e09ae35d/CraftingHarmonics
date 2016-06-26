var Items = {
    block: function() {
        return new PreventItemOperation(_.flatten(Array.prototype.slice.call(arguments)));
    }
}

Items.prevent = Items.preventItem = Items.preventUse = Items.block;