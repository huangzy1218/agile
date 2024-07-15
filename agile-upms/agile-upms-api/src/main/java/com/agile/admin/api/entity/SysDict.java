package com.agile.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Dictionary type.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Dictionary type")
@EqualsAndHashCode(callSuper = true)
public class SysDict extends Model<SysDict> {

    private static final long serialVersionUID = -3755821496674408324L;

    /**
     * ID.
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "Dictionary ID")
    private Long id;

    /**
     * Type.
     */
    @Schema(description = "Dictionary type")
    private String dictType;

    /**
     * 描述
     */
    @Schema(description = "字典描述")
    private String description;

    /**
     * Creation time
     */
    @Schema(description = "Creation time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    @Schema(description = "Update time")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * System built-in or not.
     */
    @Schema(description = "Built-in or not")
    private String systemFlag;

    /**
     * Note information.
     */
    @Schema(description = "Note information")
    private String remarks;

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
     * Delete tag
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Delete the flag,1: deleted,0: normal")
    private String delFlag;

}
    