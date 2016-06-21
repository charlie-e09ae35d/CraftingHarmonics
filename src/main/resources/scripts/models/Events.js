/*
 * Event wrapper
 */
var Event = function(type, data) {
    this.data = {type: type};
    if(data) _.assign(this.data, data);

    // Extend this with all our dynamically generated matchers
    _.assign(this, Matchers);
}

Event.prototype.on = function(name, callback) {
    name = "on" + name[0].toUpperCase() + name.slice(1);
    this.data[name] = _.concat(this.data[name] || [], callback);
    return this;
}

var Potion = function(data) {
    Event.call(this, "applyPotion", data);
}

Potion.prototype = Object.create(Event.prototype);
Potion.prototype.constructor = Potion;
