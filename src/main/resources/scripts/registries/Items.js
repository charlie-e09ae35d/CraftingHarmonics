var Items = {
    block: function() {
        return new PreventItemOperation(_.flatten(Array.prototype.slice.call(arguments)));
    },
    rename: function(what, name) {
        var op = new Operation("rename");
        op.op.what = what;
        if(name) op.op.name = name;

        op.to = function(name) {
            this.op.name = name;
            return this;
        }

        return op;
    }
}

Items.prevent = Items.preventItem = Items.preventUse = Items.block;