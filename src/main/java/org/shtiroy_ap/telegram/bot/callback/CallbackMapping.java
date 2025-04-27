package org.shtiroy_ap.telegram.bot.callback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CallbackMapping {
    String value();
}
