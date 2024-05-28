package com.agile.plugin.excel.read;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Huang Z.Y.
 */
@Data
public class NameData {

    /**
     * Read first column.
     */
    @ExcelProperty("column1")
    private String a;

    /**
     * Read second column.
     */
    @ExcelProperty("column2")
    private String b;

    /**
     * Read third column.
     */
    @ExcelProperty("column3")
    private String c;

}
    