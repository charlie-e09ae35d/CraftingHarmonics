var Operation = function(type) {
    this.op = {};
    this.op.type = type;
}

Operation.prototype.priority = function(priority) {
    this.op.priority = priority;
}