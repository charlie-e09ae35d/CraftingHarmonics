var Set = function(name) {
    this.name = name;
    this.__internal = SetManager.registerSet(name);
};

/*
 * Extend our internal operations
 */
Set.prototype.addOperation = Set.prototype.add = function(op) {
    this.__internal.addOperation(op.op ? op.op : op);
}

Set.prototype.setDuration = function(duration) {
    this.__internal.setDuration(duration);
}

Set.prototype.setCooldown = function(cooldown) {
    this.__internal.setCooldown(cooldown);
}

Set.prototype.removesSets = function(sets) {
    if(!Array.isArray(sets)) sets = [sets];
    this.__internal.setRemovedSets(sets);
}