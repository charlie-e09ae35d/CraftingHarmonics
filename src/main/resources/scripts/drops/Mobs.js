var Mobs = {
    setDropsFor: function() {
        // Get our objects into a single array, regardless of if they're an array or separate args:
        var what = _.concat(Array.prototype.slice.call(arguments));

        return new MobDropOperation(what);
    },
    addShedsFor: function() {
        return new MobShedOperation(_.concat(Array.prototype.slice.call(arguments)));
    }
}

Mobs.setDrops = Mobs.setDropsFor;
Mobs.setSheds = Mobs.setShedsFor = Mobs.addSheds = Mobs.addShedsFor;