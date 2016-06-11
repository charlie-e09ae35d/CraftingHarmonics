var CraftingOperation = function(type, output) {
    this.type = type;
    this.output = output;
}

 // Actual methods...
CraftingOperation.prototype.with = function() {
    // Oh JavaScript and your argument silliness.
    this.with = Array.prototype.slice.call(arguments);
    return this;
}

CraftingOperation.prototype.width = function(width) {
    this.width = width;
    return this;
}

CraftingOperation.prototype.height = function(height) {
    this.height = height;
    return this;
}

CraftingOperation.prototype.name = function(name) {
    this.displayName = name;
    return this;
}

CraftingOperation.prototype.withNbt = function(nbt) {
    this.nbt = nbt;
    return this;
}

CraftingOperation.prototype.quantity = function(qty) {
    this.quantity = qty;
    return this;
}

/*
 * Remove operations...
 */
var RemoveOperation = function(what) {
    this.type = "remove";
    this.what = what;
}

RemoveOperation.prototype = CraftingOperation.prototype;
RemoveOperation.prototype.constructor = RemoveOperation;

RemoveOperation.prototype.from = function() {
    this.from = Array.prototype.slice.call(arguments);
    return this;
}
