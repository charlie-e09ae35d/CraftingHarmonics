package org.winterblade.minecraft.harmony.api;

/**
 * Container for running callbacks; if implementing a callback that has its own callbacks, it should implement this
 * interface as opposed to IEntityCallback itself.
 *
 * Do not provide an implementation of this, as the internal system will ignore it.
 */
public interface IEntityCallbackContainer extends IEntityCallback {}

