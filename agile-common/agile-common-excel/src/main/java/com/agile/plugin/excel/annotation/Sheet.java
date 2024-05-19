package com.agile.plugin.excel.annotation;

import java.lang.annotation.*;

/**
 * @author Huang Z.Y.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sheet {

    /**
     * Sheet number.
     */
    int sheetNo() default -1;

    /**
     * Sheet name.
     */
    String sheetName();

    /**
     * Include field.
     */
    String[] includes() default {};

    /**
     * Exclude field.
     */
    String[] excludes() default {};

}
