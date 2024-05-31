package com.agile.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Client information.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Client information")
@EqualsAndHashCode(callSuper = true)
public class SysOauthClientDetails extends Model<SysOauthClientDetails> {

    @Serial
    private static final long serialVersionUID = 2691386627L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;

    /**
     * Client ID.
     */
    @NotBlank(message = "client_id can not be empty")
    @Schema(description = "Client ID")
    private String clientId;

    /**
     * Client key.
     */
    @NotBlank(message = "client_secret can not be empty")
    @Schema(description = "Client key")
    private String clientSecret;

    /**
     * Resource ID.
     */
    @Schema(description = "Resource ID list")
    private String resourceIds;

    /**
     * Scope.
     */
    @NotBlank(message = "scope can not be empty")
    @Schema(description = "Scope")
    private String scope;

    /**
     * Authorization mode[A,B,C]
     */
    @Schema(description = "Authorization mode")
    private String[] authorizedGrantTypes;

    /**
     * Authority.
     */
    @Schema(description = "Authority list")
    private String authorities;

    /**
     * Request token validity time.
     */
    @Schema(description = "Request token validity time")
    private Integer accessTokenValidity;

    /**
     * Request token validity time..
     */
    @Schema(description = "Request token validity time")
    private Integer refreshTokenValidity;

    /**
     * Extension information.
     */
    @Schema(description = "Extension information")
    private String additionalInformation;

    /**
     * Delete flag.
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = " Delete flag, 1 for deleted ad 0 for normal")
    private String delFlag;

    /**
     * Creator.
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Creator")
    private String createBy;

    /**
     * Modifier.
     */
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "Modifier")
    private String updateBy;

    /**
     * Create time.
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Create time")
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "Update time")
    private LocalDateTime updateTime;

}
    