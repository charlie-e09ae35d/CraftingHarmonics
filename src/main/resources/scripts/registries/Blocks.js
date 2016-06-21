var Blocks = {
    setDropsFor: function(what, state) {
        return new BlockDropOperation(what, state);
    },
    addEventsFor: function() {
        return new TileEntityEffectOperation(_.concat(Array.prototype.slice.call(arguments)));
    }
}

Blocks.setDrops = Blocks.setDropsFor;
Blocks.addEvent = Blocks.addEventFor = Blocks.addEvents = Blocks.addEventsFor;