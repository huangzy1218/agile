package com.agile.admin.api.vo;

import com.agile.plugin.excel.annotation.ExcelLine;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Post import and export.
 *
 * @author Huang Z.Y.
 */
@Data
@ColumnWidth(30)
public class PostExcelVO implements Serializable {

    private static final long serialVersionUID = -8743341383760592311L;

    /**
     * The line number is displayed during the import.
     */
    @ExcelLine
    @ExcelIgnore
    private Long lineNum;

    /**
     * Primary key ID.
     */
    @ExcelProperty("Post number")
    private Long postId;

    /**
     * Job title.
     */
    @NotBlank(message = "The post name cannot be empty")
    @ExcelProperty("Job title")
    private String postName;

    /**
     * Job identification.
     */
    @NotBlank(message = "The job ID cannot be empty")
    @ExcelProperty("Job identification")
    private String postCode;

    /**
     * Position ranking.
     */
    @NotNull(message = "The post order cannot be empty")
    @ExcelProperty("Position ranking")
    private Integer postSort;

    /**
     * Job description.
     */
    @NotBlank(message = "The job description should not be empty")
    @ExcelProperty(value = "Job description")
    private String remark;

    /**
     * Creation time.
     */
    @ExcelProperty(value = "Creation time")
    private LocalDateTime createTime;

}
    