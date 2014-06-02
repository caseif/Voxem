package com.headswilllol.mineflat.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Metadata used to denote an event listener.
 * All listener methods must use this annotation so as to be notified of events.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
}
