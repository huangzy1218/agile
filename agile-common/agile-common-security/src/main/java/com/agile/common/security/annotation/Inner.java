package com.agile.common.security.annotation;

import java.lang.annotation.*;

/**
 * Service invocation does not authenticate annotations.
 *
 * @author Huang Z.Y.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inner {

    /**
     * Whether AOP unified processing.
     *
     * @return false, true
     */
    boolean value() default true;

    /**
     * Fields requiring special null (reserved).
     *
     * @return String array
     */
    String[] field() default {};

}
