var CraftingOperation = function(type, output) {
    Operation.call(this, type);
    this.output = output;
}

// Inheritance and such...
CraftingOperation.prototype = Object.create(Operation.prototype);
CraftingOperation.prototype.constructor = CraftingOperation;

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