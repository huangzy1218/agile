package com.agile.common.log.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解。
 *
 * @author Huang Z.Y.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AgileLog {

    /**
     * 描述
     */
    String value() default "";

    /**
     * SPEL表达式
     *
     * @return 日志描述
     */
    String expression() default "";
}
