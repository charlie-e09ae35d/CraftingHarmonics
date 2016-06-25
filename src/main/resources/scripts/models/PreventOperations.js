/*
 * To prevent block placements
 */
var PreventBlockOperation = function(what) {
    Operation.call(this, "preventBlock");
    this.op.what = what;
}

PreventBlockOperation.prototype = Object.create(Operation.prototype);
PreventBlockOperation.prototype.constructor = PreventBlockOperation;

/*
 * To prevent item usage
 */
var PreventItemOperation = function(what) {
    Operation.call(this, "preventItem");
    this.op.what = what;
    this.data = this.op; // For matchers.
    _.assign(this, Matchers);
}

PreventItemOperation.prototype = Object.create(Operation.prototype);
PreventItemOperation.prototype.constructor = PreventItemOperation;

PreventItemOperation.prototype.onBlock = function() {
    this.op.onBlock = _.concat(this.op.onBlock || [], _.flatten(Array.prototype.slice.call(arguments)));
    return this;
}