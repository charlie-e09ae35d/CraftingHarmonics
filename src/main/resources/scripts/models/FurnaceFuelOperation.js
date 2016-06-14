var FurnaceFuel = function(what, time) {
    Operation.call(this, "addFurnaceFuel");
    this.op.what = what;
    if(time) this.op.burnTime = time;
}

FurnaceFuel.prototype = Object.create(Operation.prototype);
FurnaceFuel.prototype.constructor = FurnaceFuel;

FurnaceFuel.prototype.burnTime = function(time) {
    this.op.burnTime = time;
    return this;
}