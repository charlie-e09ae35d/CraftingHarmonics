/*
 * To prevent block placements
 */
var PreventBlockOperation = function() {
    Operation.call(this, "preventBlock");
    this.op.what = _.flatten(Array.prototype.slice.call(arguments));
}

PreventBlockOperation.prototype = Object.create(Operation.prototype);
PreventBlockOperation.prototype.constructor = PreventBlockOperation;
