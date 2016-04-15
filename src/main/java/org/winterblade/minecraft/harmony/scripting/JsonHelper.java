package org.winterblade.minecraft.harmony.scripting;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * Created by Matt on 4/9/2016.
 */
public class JsonHelper {
    private static ScriptObjectMirror internalObj;

    private JsonHelper() {}

    /**
     * Callback from our script in order to allow us to register the internal object from Nashorn.
     * @param internal  The internal object
     */
    public static void registerInternal(ScriptObjectMirror internal) {
        internalObj = internal;
    }

    /**
     * Used to translate an object into a JSON string that can be read in by Minecraft (mainly for parsing NBT)
     * @param o The object to parse
     * @return  The JSON string.
     */
    public static String getJsonString(Object o) {
        if(internalObj == null) {
            System.err.println("Internal object hasn't been registered yet.");
            return "";
        }

        try {
            return internalObj.callMember("getJsonString", o).toString();
        } catch(Exception e) {
            System.err.println("Internal object doesn't have method getJsonString.");
            return "";
        }
    }
}
