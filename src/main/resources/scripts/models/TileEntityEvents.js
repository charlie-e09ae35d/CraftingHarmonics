/*
 * The generic effect operation itself
 */
var TileEntityEffectOperation = function(what) {
    Operation.call(this, "addTileEntityEvent");
    this.op.what = what;
}

TileEntityEffectOperation.prototype = Object.create(Operation.prototype);
TileEntityEffectOperation.prototype.constructor = TileEntityEffectOperation;

TileEntityEffectOperation.prototype.addEvent = function(effect) {
    if(!this.op.events) this.op.events = [];
    this.op.events.push(effect.data ? effect.data : effect);
    return this;
}

// Alias it
TileEntityEffectOperation.prototype.addEffect = TileEntityEffectOperation.prototype.addEvent;
