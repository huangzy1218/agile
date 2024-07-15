package com.agile.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Role table.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Role")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 102833674477309739L;

    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    @Schema(description = "Role ID")
    private Long roleId;

    @NotBlank(message = "The role name cannot be empty")
    @Schema(description = "Role name")
    private String roleName;

    @NotBlank(message = "The role identifier cannot be empty")
    @Schema(description = "Role identification")
    private String roleCode;

    @Schema(description = "Role description")
    private String roleDesc;

    /**
     * Founder.
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Founder")
    private String createBy;

    /**
     * Modifier
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
     * Modification time.
     */
    @Schema(description = "Modification time")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * Delete identifier (0- normal,1- delete).
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Delete the flag,1: deleted,0: normal")
    private String delFlag;

}
    