var Blocks = {
    setDropsFor: function(what, state) {
        return new BlockDropOperation(what, state);
    },
    addEventsFor: function() {
        return new TileEntityEffectOperation(_.concat(Array.prototype.slice.call(arguments)));
    },
    block: function() {
        return new PreventBlockOperation(_.flatten(Array.prototype.slice.call(arguments)));
    },
    setLightLevel: function(what, light) {
        var op = new Operation("setLightLevel");
        op.op.what = what;
        op.op.lightLevel = light;
        return op;
    },
    setHardness: function(what, hardness) {
        var op = new Operation("setBlockHardness");
        op.op.what = what;
        op.op.hardness = hardness;
        return op;
    }
}

Blocks.setDrops = Blocks.setDropsFor;
Blocks.addEvent = Blocks.addEventFor = Blocks.addEvents = Blocks.addEventsFor;
Blocks.prevent = Blocks.preventBlock = Blocks.block;
Blocks.light = Blocks.setLight = Blocks.setLightLevel;
Blocks.hardness = Blocks.setHardness;