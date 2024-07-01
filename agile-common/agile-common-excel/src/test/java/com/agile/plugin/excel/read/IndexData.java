package com.agile.plugin.excel.read;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Specifies the column subscript or column name test entity.
 *
 * @author Huang Z.Y.
 */
@Data
@ToString
public class IndexData {

    /**
     * Read first column.
     */
    @ExcelProperty(index = 0)
    private String a;

    /**
     * Read second column.
     */
    @ExcelProperty(index = 1)
    private String b;

    /**
     * Read third column.
     */
    @ExcelProperty(index = 2)
    private String c;

}
    