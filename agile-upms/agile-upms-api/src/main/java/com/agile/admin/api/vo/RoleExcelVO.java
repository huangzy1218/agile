package com.agile.admin.api.vo;

import com.agile.plugin.excel.annotation.ExcelLine;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Role import and export
 *
 * @author Huang Z.Y.
 */
@Data
@ColumnWidth(30)
public class RoleExcelVO implements Serializable {

    private static final long serialVersionUID = -5153038511276086962L;

    /**
     * The line number is displayed during the import.
     */
    @ExcelLine
    @ExcelIgnore
    private Long lineNum;

    /**
     * Primary key ID.
     */
    @ExcelProperty("Role number")
    private Long roleId;

    /**
     * Role name.
     */
    @NotBlank(message = "The role name cannot be empty")
    @ExcelProperty("Role name")
    private String roleName;

    /**
     * Role ID.
     */
    @NotBlank(message = "The role identifier cannot be empty")
    @ExcelProperty("Role identification")
    private String roleCode;

    /**
     * Role description.
     */
    @NotBlank(message = "The role description cannot be empty")
    @ExcelProperty("Role description")
    private String roleDesc;

    /**
     * Creation time.
     */
    @ExcelProperty(value = "Creation time")
    private LocalDateTime createTime;

}

    