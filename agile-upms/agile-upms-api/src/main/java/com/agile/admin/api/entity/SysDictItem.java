package com.agile.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Dictionary item.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Dictionary entry")
@EqualsAndHashCode(callSuper = true)
public class SysDictItem extends Model<SysDictItem> {

    private static final long serialVersionUID = -7477499442529811434L;

    /**
     * ID.
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    /**
     * ID of the dictionary class it belongs to.
     */
    @Schema(description = "ID of the dictionary class it belongs to")
    private Long dictId;

    /**
     * Data value.
     */
    @Schema(description = "Data value")
    @JsonProperty(value = "value")
    private String itemValue;

    /**
     * Tag name.
     */
    @Schema(description = "Tag name")
    private String label;

    /**
     * Type.
     */
    @Schema(description = "Type")
    private String dictType;

    /**
     * Description.
     */
    @Schema(description = "Description")
    private String description;

    /**
     * Sort (ascending).
     */
    @Schema(description = "Sort (ascending)")
    private Integer sortOrder;

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

    /**
     * Note information.
     */
    @Schema(description = "Note information")
    private String remarks;

    /**
     * Delete tag.
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Delete the flag,1: deleted,0: normal")
    private String delFlag;

}
    