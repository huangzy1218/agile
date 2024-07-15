package com.agile.admin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Front-end log display objects.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Front-end log display objects")
public class PreLogVO {

    /**
     * Request url.
     */
    @Schema(description = "Request url")
    private String url;

    /**
     * Request time.
     */
    @Schema(description = "Request time")
    private String time;

    /**
     * Requesting user.
     */
    @Schema(description = "Requesting user")
    private String user;

    /**
     * Request result.
     */
    @Schema(description = "Request result 0: successful 9: failed")
    private String type;

    /**
     * Request pass parameter.
     */
    @Schema(description = "Request pass parameter")
    private String message;

    /**
     * Exception message
     */
    @Schema(description = "Exception message")
    private String stack;

    /**
     * Log title.
     */
    @Schema(description = "Log title")
    private String info;

}

    