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
    },
    setDurability: function(what, value) {
        var op = new Operation("setItemDurability");
        op.op.what = what;
        if(value) op.op.durability = value;

        op.to = function(value) {
            this.op.durability = value;
            return this;
        }

        return op;
    }
}

Items.prevent = Items.preventItem = Items.preventUse = Items.block;