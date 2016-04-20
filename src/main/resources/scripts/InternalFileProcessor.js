// For our actual recipes object
var module = { exports: {} };

var __CraftingHarmonicsInternal_Internal = function() {
    var ch = this;

    ch.RecipeOperationRegistry = Java.type('org.winterblade.minecraft.harmony.crafting.RecipeOperationRegistry');

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

                if(!ch.RecipeOperationRegistry.CreateOperationInSet(set.name,op.type, op)) {
                    print("Invalid operation: #" + j + " in set.");
                }
            }
        }
    };
}

// Get our internal instance:
__CraftingHarmonicsInternal = new __CraftingHarmonicsInternal_Internal();