package com.agile.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Log query transmission object,
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Log query object")
public class SysLogDTO {

    /**
     * ID.
     */
    private Long id;

    /**
     * Log type.
     */
    @NotBlank(message = "The log type cannot be empty")
    private String logType;

    /**
     * Log title.
     */
    @NotBlank(message = "The log title cannot be empty")
    private String title;

    /**
     * Creator.
     */
    private String createBy;

    /**
     * Update time.
     */
    private LocalDateTime updateTime;

    /**
     * Operating IP address.
     */
    private String remoteAddr;

    /**
     * User agent.
     */
    private String userAgent;

    /**
     * Request URI.
     */
    private String requestUri;

    /**
     * Mode of operation.
     */
    private String method;

    /**
     * Manipulate the submitted data.
     */
    private String params;

    /**
     * Execution time.
     */
    private Long time;

    /**
     * Exception message.
     */
    private String exception;

    /**
     * Service ID.
     */
    private String serviceId;

    /**
     * Create time range [start time, end time]
     */
    private LocalDateTime[] createTime;

}
    