var RegisterOreDictionary = function(what, dict) {
    Operation.call(this, "registerOreDictItem");
    this.op.what = what;
    if(dict) this.op.oreDict = dict;
}

RegisterOreDictionary.prototype = Object.create(Operation.prototype);
RegisterOreDictionary.prototype.constructor = RegisterOreDictionary;

RegisterOreDictionary.prototype.oreDict = function(dict) {
    this.op.oreDict = dict;
    return this;
}