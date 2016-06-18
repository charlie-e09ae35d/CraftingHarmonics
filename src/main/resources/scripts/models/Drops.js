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
    this.op.drops.push(drop.data ? drop.data : drop);
    return this;
}


/*
 * Actual drop object
 */
var Drop = function(what) {
    this.data = {what: what};
    // Extend this with all our dynamically generated matchers
    _.assign(this, Matchers);
}


/*
 * Mob drops
 */
var MobDropOperation = function(what) {
    DropOperation.call(this, "setMobDrops", what);
}

MobDropOperation.prototype = Object.create(DropOperation.prototype);
MobDropOperation.prototype.constructor = MobDropOperation;