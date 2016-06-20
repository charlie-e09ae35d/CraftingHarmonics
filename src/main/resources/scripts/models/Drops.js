/*
 * The generic drop operation itself
 */
var DropOperation = function(type, what) {
    Operation.call(this, type);
    this.op.what = what;
}

DropOperation.prototype = Object.create(Operation.prototype);
DropOperation.prototype.constructor = DropOperation;

DropOperation.prototype.replace = function(isReplace) {
    this.op.replace = isReplace;
    return this;
}

DropOperation.prototype.addDrop = function(drop) {
    this.op.drops = this.op.drops || [];
    this.op.sheds = this.op.drops;
    this.op.drops.push(drop.data ? drop.data : drop);
    return this;
}

/*
 * Actual drop object
 */
var Drop = function(what, min, max) {
    this.data = {what: what, min: min || 1, max: max || 1};
    // Extend this with all our dynamically generated matchers
    _.assign(this, Matchers);
}

Drop.prototype.min = function(val) {
    this.data.min = val;
    return this;
}

Drop.prototype.max = function(val) {
    this.data.max = val;
    return this;
}

Drop.prototype.lootingMultiplier = function(val) {
    this.data.lootingMultiplier = val;
    return this;
}

Drop.prototype.fortuneMultiplier = function(val) {
    this.data.fortuneMultiplier = val;
    return this;
}

Drop.prototype.otherwise = function(drop) {
    this.data.otherwise = drop.data ? drop.data : drop;
    return this;
}


/*
 * Mob drops
 */
var MobDropOperation = function(what) {
    DropOperation.call(this, "setMobDrops", what);
}

MobDropOperation.prototype = Object.create(DropOperation.prototype);
MobDropOperation.prototype.constructor = MobDropOperation;

MobDropOperation.prototype.exclude = function() {
    this.op.exclude = _.concat(Array.prototype.slice.call(arguments));
    return this;
}

MobDropOperation.prototype.includePlayers = MobDropOperation.prototype.includePlayerDrops = function(inc) {
    this.op.includePlayerDrops = inc;
    return this;
}

/*
 * Mob shedding.
 */
var MobShedOperation = function(what) {
    DropOperation.call(this, "addMobShed", what);
}

MobShedOperation.prototype = Object.create(DropOperation.prototype);
MobShedOperation.prototype.constructor = MobShedOperation;

MobShedOperation.prototype.addShed = DropOperation.prototype.addDrop;