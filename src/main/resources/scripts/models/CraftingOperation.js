var CraftingOperation = function(type, output) {
    Operation.call(this, type);
    this.op.output = output;
}

CraftingOperation.prototype = Object.create(Operation.prototype);
CraftingOperation.prototype.constructor = CraftingOperation;

 // Actual methods...
CraftingOperation.prototype.with = function() {
    // Oh JavaScript and your argument silliness.
    this.op.with = Array.prototype.slice.call(arguments);
    return this;
}

CraftingOperation.prototype.width = function(width) {
    this.op.width = width;
    return this;
}

CraftingOperation.prototype.height = function(height) {
    this.op.height = height;
    return this;
}

CraftingOperation.prototype.name = function(name) {
    this.op.displayName = name;
    return this;
}

CraftingOperation.prototype.withNbt = function(nbt) {
    this.op.nbt = nbt;
    return this;
}

CraftingOperation.prototype.quantity = function(qty) {
    this.op.quantity = qty;
    return this;
}

/*
 * Remove operations...
 */
var RemoveOperation = function(what) {
    CraftingOperation.call(this, "remove", "");
    this.op.what = what;
}

RemoveOperation.prototype = Object.create(CraftingOperation.prototype);
RemoveOperation.prototype.constructor = RemoveOperation;

RemoveOperation.prototype.from = function() {
    this.op.from = Array.prototype.slice.call(arguments);
    return this;
}


/*
 * Furnace
 */
var FurnaceOperation = function(output) {
    CraftingOperation.call(this, "addFurnace", output);
}

FurnaceOperation.prototype = Object.create(CraftingOperation.prototype);
FurnaceOperation.prototype.constructor = FurnaceOperation;

FurnaceOperation.prototype.xp = FurnaceOperation.prototype.experience = function(xp) {
    this.op.experience = xp;
    return this;
}

FurnaceOperation.prototype.with = function(input) {
    this.op.with = input;
    return this;
}

/*
 * Add brews
 */
var AddBrewOperation = function(what, input, ingredient) {
    CraftingOperation.call(this, "addBrew", what);
    if(input) this.op.input = input;
    if(ingredient) this.op.ingredient = ingredient;
}

AddBrewOperation.prototype = Object.create(CraftingOperation.prototype);
AddBrewOperation.prototype.constructor = AddBrewOperation;

AddBrewOperation.prototype.input = function(input) {
    this.op.input = time;
    return this;
}

AddBrewOperation.prototype.ingredient = function(ingredient) {
    this.op.ingredient = ingredient;
    return this;
}