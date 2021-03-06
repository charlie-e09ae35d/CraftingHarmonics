// For our actual recipes object
var module = { exports: {} };

var interop = {};

var SetManager = Java.type('org.winterblade.minecraft.harmony.SetManager');

function __CraftingHarmonicsInternalAddInterop(javaClass, interopPath) {
    var interopEntry = interop;

    // Build up to our proper interop path:
    for(var i = 0; i < interopPath.length - 1; i++) {
        if(!interopEntry[interopPath[i]]) interopEntry[interopPath[i]] = {};
        interopEntry = interopEntry[interopPath[i]];
    }

    // Assign it:
    interopEntry[interopPath[interopPath.length-1]] = Java.type(javaClass);
}

var __CraftingHarmonicsInternal_Internal = function() {
    var ch = this;

    // This will run in order to actually get our recipe data into something sensible
    this.FileProcessor = function(filename, exports) {
        print("Processing script file '" + filename + "'...");

        if(!exports) {
            print("Couldn't get the data from this file.");
            return;
        }

        try {
            ch.ProcessSets(exports.sets || []);
        } catch(e) {
            print("Error reading sets in the file.", e.message);
        }
    };

    /*
     * Actually process in our sets.
     */
    ch.ProcessSets = function(sets) {
        for(var i in sets) {
            if(!sets.hasOwnProperty(i)) continue;
            var set = sets[i];

            print("Found set '" + set.name + "'.");
            var opSet = SetManager.registerSet(set.name);

            if(!set.operations || !(set.operations instanceof Array)) {
                print("Set has no operations array on it.");
                continue;
            }

            for(var j in set.operations) {
                if(!set.operations.hasOwnProperty(j) || !set.operations[j]) continue;

                var op = set.operations[j];
                if(!op || !op.type) {
                    print("Invalid operation: #" + j + " in set.");
                    continue;
                }

                if(!opSet.addOperation(op)) {
                    print("Unable to create operation: #" + j + " (" + op.type + ") in set.");
                }
            }

            // Deal with set metadata:
            if(set.duration) opSet.setDuration(set.duration);
            if(set.cooldown) opSet.setCooldown(set.cooldown);
            if(set.removesSets) opSet.setRemovedSets(set.removesSets);
        }
    };
}

// Get our internal instance:
__CraftingHarmonicsInternal = new __CraftingHarmonicsInternal_Internal();