package org.winterblade.minecraft.harmony.common.utility;

import java.awt.*;

/**
 * Created by Matt on 7/27/2016.
 */
public class ColorHelper {
    private ColorHelper() {}

    public static Color convertHex(String color) {
            if(color == null || color.equals("") || color.length() != 7) return new Color(255,255,255);

            Integer r = Integer.valueOf(color.substring(1, 3), 16);
            Integer g = Integer.valueOf(color.substring(3, 5), 16);
            Integer b = Integer.valueOf(color.substring(5, 7), 16);

            return new Color(r, g, b);
    }
}
