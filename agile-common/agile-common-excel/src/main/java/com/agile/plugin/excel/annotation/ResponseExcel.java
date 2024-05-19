package com.agile.plugin.excel.annotation;

import com.alibaba.excel.support.ExcelTypeEnum;

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
     * 包含字段
     *
     * @return String[]
     */
    String[] include() default {};

    /**
     * 排除字段
     *
     * @return String[]
     */
    String[] exclude() default {};

}
