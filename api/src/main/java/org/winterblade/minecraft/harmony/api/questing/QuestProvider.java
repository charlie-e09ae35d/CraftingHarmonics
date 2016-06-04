package org.winterblade.minecraft.harmony.api.questing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that this class implements the IQuestProvider interface and should reegister itself for providing
 * access to the questing system.
 */
@Target(ElementType.TYPE)
public @interface QuestProvider {}
