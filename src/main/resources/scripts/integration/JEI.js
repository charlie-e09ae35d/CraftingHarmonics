var JEI = {
    hide: function(what) {
        return new HideJeiOperation(what);
    },
    show: function(what) {
        return new ShowJeiOperation(what);
    }
}


// Hide...
var HideJeiOperation = function(what) {
    Operation.call(this, "hide");
    this.op.what = what;
}

HideJeiOperation.prototype = Object.create(Operation.prototype);
HideJeiOperation.prototype.constructor = HideJeiOperation;


// Show...
var ShowJeiOperation = function(what) {
    Operation.call(this, "show");
    this.op.what = what;
}

ShowJeiOperation.prototype = Object.create(Operation.prototype);
ShowJeiOperation.prototype.constructor = ShowJeiOperation;