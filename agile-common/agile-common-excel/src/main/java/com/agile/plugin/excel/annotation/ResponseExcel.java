package com.agile.plugin.excel.annotation;

import com.agile.plugin.excel.head.HeadGenerator;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;

import java.lang.annotation.*;

/**
 * Export excel.
 *
 * @author Huang Z.Y.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseExcel {

    /**
     * File name.
     *
     * @return string
     */
    String name() default "";

    /**
     * File type (xlsx xls)
     *
     * @return string
     */
    ExcelTypeEnum suffix() default ExcelTypeEnum.XLSX;

    /**
     * File name.
     *
     * @return password
     */
    String password() default "";

    /**
     * Sheet name, contains multiple.
     *
     * @return String[]
     */
    Sheet[] sheets() default @Sheet(sheetName = "sheet1");

    /**
     * Memory operation.
     *
     * @return boolean
     */
    boolean inMemory() default false;

    /**
     * Excel template.
     *
     * @return String
     */
    String template() default "";

    /**
     * Include field.
     *
     * @return String[]
     */
    String[] include() default {};

    /**
     * Exclusion field.
     *
     * @return String[]
     */
    String[] exclude() default {};

    /**
     * Internationalize the excel header.
     *
     * @return boolean
     */
    boolean i18nHeader() default false;

    /**
     * Custom Excel header generator.
     *
     * @return HeadGenerator
     */
    Class<? extends HeadGenerator> headGenerator() default HeadGenerator.class;

    /**
     * Fill mode.
     *
     * @return true or false
     */
    boolean fill() default false;

    /**
     * Interceptors, custom styles and other processors
     *
     * @return WriteHandler array
     */
    Class<? extends WriteHandler>[] writeHandler() default {};

    /**
     * Converter.
     *
     * @return Converter[]
     */
    Class<? extends Converter>[] converter() default {};

}
