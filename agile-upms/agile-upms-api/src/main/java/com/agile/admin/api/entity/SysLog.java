package com.agile.admin.api.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Log sheet.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Log")
public class SysLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 2691386627L;

    /**
     * ID.
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ExcelProperty("log id")
    @Schema(description = "log id")
    private Long id;

    /**
     * Log type.
     */
    @NotBlank(message = "The log type cannot be empty")
    @ExcelProperty("Log type (0-Normal 9- Error)")
    @Schema(description = "Log type")
    private String logType;

    /**
     * Log title.
     */
    @NotBlank(message = "The log title cannot be empty")
    @ExcelProperty("Log title")
    @Schema(description = " Log title")
    private String title;

    /**
     * Creator.
     */
    @ExcelProperty("Creator")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Creator")
    private String createBy;

    /**
     * Create time.
     */
    @ExcelProperty("Create time")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Create time")
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    @ExcelIgnore
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "Update time")
    private LocalDateTime updateTime;

    /**
     * Operating IP address.
     */
    @ExcelProperty("Operating IP address")
    @Schema(description = "Operating IP address")
    private String remoteAddr;

    /**
     * User agent.
     */
    @Schema(description = "User agent")
    private String userAgent;

    /**
     * Request URI.
     */
    @ExcelProperty("Request URI")
    @Schema(description = "Request URI")
    private String requestUri;

    /**
     * Mode of operation.
     */
    @ExcelProperty("Mode of operation")
    @Schema(description = "Mode of operation")
    private String method;

    /**
     * Manipulate the submitted data.
     */
    @ExcelProperty("Manipulate the submitted data")
    @Schema(description = "Manipulate the submitted data")
    private String params;

    /**
     * Execution time.
     */
    @ExcelProperty("Execution time")
    @Schema(description = "Execution time")
    private Long time;

    /**
     * Exception message.
     */
    @ExcelProperty("Exception message")
    @Schema(description = "Exception message")
    private String exception;

    /**
     * Service ID.
     */
    @ExcelProperty("Application ID")
    @Schema(description = "Application ID")
    private String serviceId;

    /**
     * Delete tag.
     */
    @TableLogic
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Delete tag, 1 deleted, 0 normal")
    private String delFlag;

}
    