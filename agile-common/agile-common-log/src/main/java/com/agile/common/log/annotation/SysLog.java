package com.agile.common.log.annotation;

import java.lang.annotation.*;

/**
 * Operation log annotation.
 *
 * @author Huang Z.Y.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * description.
     */
    String value() default "";

    /**
     * SpEL expression.
     *
     * @return Log description
     */
    String expression() default "";
}
