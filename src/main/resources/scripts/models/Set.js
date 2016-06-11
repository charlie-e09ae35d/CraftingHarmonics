var Set = function(name) {
    this.name = name;
    this.__internal = SetManager.registerSet(name);
};

/*
 * Extend our internal operations
 */
Set.prototype.addOperation = function(op) {
    this.__internal.addOperation(op);
}

Set.prototype.setDuration = function(duration) {
    this.__internal.setDuration(duration);
}

Set.prototype.setCooldown = function(cooldown) {
    this.__internal.setCooldown(cooldown);
}