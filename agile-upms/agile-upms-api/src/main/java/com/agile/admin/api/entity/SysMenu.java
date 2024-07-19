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
 * System menu.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Menu")
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = -3115129227969638656L;

    /**
     * Menu ID.
     */
    @TableId(value = "menu_id", type = IdType.ASSIGN_ID)
    @Schema(description = "Menu ID")
    private Long menuId;

    /**
     * Menu name.
     */
    @NotBlank(message = "Menu name cannot be empty")
    @Schema(description = "Menu name")
    private String name;

    /**
     * Menu name.
     */
    @Schema(description = "Menu name")
    private String enName;

    /**
     * Menu permission identifier
     */
    @Schema(description = "Menu permission identifier")
    private String permission;

    /**
     * Parent menu ID.
     */
    @NotNull(message = "Menu parent ID cannot be empty")
    @Schema(description = "Menu parent id")
    private Long parentId;

    /**
     * Icon.
     */
    @Schema(description = "Menu icon")
    private String icon;

    /**
     * Front-end routing identification path, the default is consistent with comment. Expired
     */
    @Schema(description = "Front-end routing identification path")
    private String path;

    /**
     * Menu show hide control.
     */
    @Schema(description = "Whether the menu is displayed")
    private String visible;

    /**
     * Sort value.
     */
    @Schema(description = "Sort value")
    private Integer sortOrder;

    /**
     * Menu type (0 menu 1 button).
     */
    @NotNull(message = "Menu type cannot be empty")
    @Schema(description = "Menu type, 0: menu 1: button")
    private String menuType;

    /**
     * Route buffer.
     */
    @Schema(description = "Route buffer")
    private String keepAlive;

    @Schema(description = "Whether the menu is embedded")
    private String embedded;

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
     * 0--Normal 1--Delete.
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "Deletion mark, 1: deleted, 0: normal")
    private String delFlag;

}

