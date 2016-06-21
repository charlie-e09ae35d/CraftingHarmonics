/*
 * The generic effect operation itself
 */
var EntityEffectOperation = function(what) {
    Operation.call(this, "addEntityEvent");
    this.op.what = what;
}

EntityEffectOperation.prototype = Object.create(Operation.prototype);
EntityEffectOperation.prototype.constructor = EntityEffectOperation;

EntityEffectOperation.prototype.addEvent = function(effect) {
    if(!this.op.events) this.op.events = [];
    this.op.events.push(effect.data ? effect.data : effect);
    return this;
}

// Alias it
EntityEffectOperation.prototype.addEffect = EntityEffectOperation.prototype.addEvent;


/*
 * The wrapper for potion effects...
 */
var MobPotionEffectOperation = function(what) {
    EntityEffectOperation.call(this, what);
}

MobPotionEffectOperation.prototype = Object.create(EntityEffectOperation.prototype);
MobPotionEffectOperation.prototype.constructor = MobPotionEffectOperation;

MobPotionEffectOperation.prototype.addPotion = function(potion) {
    if(!potion.data) potion.type = "applyPotion";
    this.addEvent(potion);
    return this;
}



/*
 * Event wrapper
 */
var Event = function(type, data) {
    this.data = {type: type};
    _.assign(this.data, data);

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
