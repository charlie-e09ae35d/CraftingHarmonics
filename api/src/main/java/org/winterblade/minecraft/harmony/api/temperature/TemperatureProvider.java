package org.winterblade.minecraft.harmony.api.temperature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that this class implements the {@link ITemperatureProvider} interface and should register itself for providing
 * access to the temperature system.
 */
@Target(ElementType.TYPE)
public @interface TemperatureProvider {}
