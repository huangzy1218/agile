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
 * Post information table.
 *
 * @author Huang Z.Y.
 */
@Data
@TableName("sys_post")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Post information table")
public class SysPost extends Model<SysPost> {

    private static final long serialVersionUID = -2508575937065468729L;

    /**
     * Post ID.
     */
    @TableId(value = "post_id", type = IdType.ASSIGN_ID)
    @Schema(description = "Post ID")
    private Long postId;

    /**
     * Post code.
     */
    @NotBlank(message = "The post code cannot be empty")
    @Schema(description = "Post code")
    private String postCode;

    /**
     * Job title.
     */
    @NotBlank(message = "The post name cannot be empty")
    @Schema(description = "Job title")
    private String postName;

    /**
     * Position ranking.
     */
    @NotNull(message = "The sort value cannot be empty")
    @Schema(description = "Position ranking")
    private Integer postSort;

    /**
     * Job description.
     */
    @Schema(description = "Job description")
    private String remark;

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
     * Delete or not -1: deleted 0: normal.
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Delete or not -1: deleted 0: normal")
    private String delFlag;

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

}
    