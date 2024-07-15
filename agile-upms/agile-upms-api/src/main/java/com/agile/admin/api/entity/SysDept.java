package com.agile.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Department management.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Department")
@EqualsAndHashCode(callSuper = true)
public class SysDept extends Model<SysDept> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "dept_id", type = IdType.ASSIGN_ID)
    @Schema(description = "Department ID")
    private Long deptId;

    /**
     * Department name.
     */
    @NotBlank(message = "The department name cannot be empty")
    @Schema(description = "Department name")
    private String name;

    /**
     * Sort.
     */
    @NotNull(message = "The sort value cannot be empty")
    @Schema(description = "Ranking value")
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
    @Schema(description = "Creation time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Modification time
     */
    @Schema(description = "Modification time")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * ID of the parent department
     */
    @Schema(description = "ID of the parent department")
    private Long parentId;

    /**
     * Whether to delete 1: deleted 0: normal.
     */
    @TableLogic
    @Schema(description = "Whether to delete 1: deleted 0: normal")
    @TableField(fill = FieldFill.INSERT)
    private String delFlag;

}

    