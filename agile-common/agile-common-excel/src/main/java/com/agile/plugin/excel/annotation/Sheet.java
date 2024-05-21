package com.agile.plugin.excel.annotation;

import com.agile.plugin.excel.head.HeadGenerator;

import java.lang.annotation.*;

/**
 * Identifies the configuration associated with a Sheet of the Excel file.
 *
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

    /**
     * Head generator.
     */
    Class<? extends HeadGenerator> headGenerateClass() default HeadGenerator.class;

}
