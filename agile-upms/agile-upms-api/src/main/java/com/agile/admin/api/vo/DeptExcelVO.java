package com.agile.admin.api.vo;

import com.agile.plugin.excel.annotation.ExcelLine;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * Department import and export.
 *
 * @author Huang Z.Y.
 */
@Data
public class DeptExcelVO implements Serializable {

    private static final long serialVersionUID = -4870735860023157143L;

    /**
     * The line number is displayed during the import.
     */
    @ExcelLine
    @ExcelIgnore
    private Long lineNum;

    /**
     * Superior department.
     */
    @NotBlank(message = "The superior department cannot be empty")
    @ExcelProperty("Superior department")
    private String parentName;

    /**
     * Department name.
     */
    @NotBlank(message = "The department name cannot be empty")
    @ExcelProperty("Department name")
    private String name;

    /**
     * Sort.
     */
    @ExcelProperty(value = "Ranking value")
    private Integer sortOrder;

}
    