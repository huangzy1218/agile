package com.agile.plugin.excel.annotation;

import com.agile.plugin.excel.handler.DefaultAnalysisEventListener;
import com.agile.plugin.excel.handler.ListAnalysisEventListener;

import java.lang.annotation.*;

/**
 * Import excel.
 *
 * @author Huang Z.Y.
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestExcel {

    /**
     * Front-end upload field name, default is file.
     */
    String fileName() default "file";

    /**
     * Read listener.
     *
     * @return readListener
     */
    Class<? extends ListAnalysisEventListener<?>> readListener() default DefaultAnalysisEventListener.class;

    /**
     * Whether to skip empty lines.
     *
     * @return Skip by default
     */
    boolean ignoreEmptyRow() default false;

    /**
     * Number of header lines read.
     */
    int headRowNumber() default 1;

}
    