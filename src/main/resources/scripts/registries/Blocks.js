var Blocks = {
    setDropsFor: function(what, state) {
        return new BlockDropOperation(what, state);
    }
}

Blocks.setDrops = Blocks.setDropsFor;
