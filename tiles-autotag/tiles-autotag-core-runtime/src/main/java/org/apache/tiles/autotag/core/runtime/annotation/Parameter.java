package org.apache.tiles.autotag.core.runtime.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface Parameter {

    static final String SAME_NAME = "USE THE SAME NAME";

    String name() default SAME_NAME;

    boolean required() default false;

    String defaultValue() default "null";
}
