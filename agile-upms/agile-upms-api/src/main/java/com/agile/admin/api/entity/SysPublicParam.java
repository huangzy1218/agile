package com.agile.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Common parameter configuration.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Common parameter")
@EqualsAndHashCode(callSuper = true)
public class SysPublicParam extends Model<SysPublicParam> {

    private static final long serialVersionUID = 4285876548394392515L;

    /**
     * Public ID.
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "Public ID")
    private Long publicId;

    /**
     * Public parameter name.
     */
    @Schema(description = "Public parameter name", example = "Public parameter name")
    private String publicName;

    /**
     * Public parameter address value in uppercase + underscore
     */
    @Schema(description = "Key [uppercase + underline]", required = true, example = "AGILE_PUBLIC_KEY")
    private String publicKey;

    /**
     * Value.
     */
    @Schema(description = "Value", required = true, example = "999")
    private String publicValue;

    /**
     * Status (1 valid; 2 invalid;)
     */
    @Schema(description = "Identifier [1 valid; 2 invalid]", example = "1")
    private String status;

    /**
     * Common parameter coding.
     */
    @Schema(description = "encode", example = "^(AGILE)$")
    private String validateCode;

    /**
     * System built-in or not.
     */
    @Schema(description = "System built-in or not")
    private String systemFlag;

    /**
     * Configuration type:
     * 0- Default. 1- Search; 2- original text; 3- Report form; 4- Safety; 5- Documentation; 6- Message; 9- Other
     */
    @Schema(description = "类型[1-检索；2-原文...]", example = "1")
    private String publicType;

    /**
     * Founder.
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Founder")
    private String createBy;

    /**
     * Modifier.
     */
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "Modifier")
    private String updateBy;

    /**
     * Delete flag..
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Delete the flag,1: deleted,0: normal")
    private String delFlag;

    /**
     * Creation time.
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Creation time")
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "Update time")
    private LocalDateTime updateTime;

}
    