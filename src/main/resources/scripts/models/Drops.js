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
}

DropOperation.prototype.addDrop = function(drop) {
    this.op.drops = this.op.drops || [];
    this.op.drops.push(drop);
}


/*
 * Mob drops
 */
var MobDropOperation = function(what) {
    DropOperation.call(this, "setMobDrops", what);
}

MobDropOperation.prototype = Object.create(DropOperation.prototype);
MobDropOperation.prototype.constructor = MobDropOperation;