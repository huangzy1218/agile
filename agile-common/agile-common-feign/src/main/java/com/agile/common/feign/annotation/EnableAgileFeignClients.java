package com.agile.common.feign.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Huang Z.Y.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableAgileFeignClients {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
     * declarations e.g.: {@code @ComponentScan("org.my.pkg")} instead of
     * {@code @ComponentScan(basePackages="org.my.pkg")}.
     *
     * @return the array of 'basePackages'
     */
    String[] value() default {};

    /**
     * Base packages to scan for annotated components.<br/>
     * {@link #value()} is an alias for (and mutually exclusive with) this attribute.
     *
     * @return the array of 'basePackages'
     */
    @AliasFor(annotation = EnableFeignClients.class, attribute = "basePackages")
    String[] basePackages() default {"com.agile"};

}
