var Matchers = {};

__addMatcher = function(name) {
    Matchers[name] = function(arg) {
        if(1 < arguments.length) {
            this.data[name] = Array.prototype.slice.call(arguments);
        } else {
            this.data[name] = arg;
        }
        return this;
    }
}
