package org.winterblade.minecraft.harmony.api.calendar;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that this class implements the ICalendarProvider interface and should register itself for providing
 * access to the calendar system.
 */
@Target(ElementType.TYPE)
public @interface CalendarProvider {}
