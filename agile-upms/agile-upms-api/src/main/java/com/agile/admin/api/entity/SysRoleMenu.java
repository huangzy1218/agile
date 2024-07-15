package com.agile.admin.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Role menu.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Role menu")
@EqualsAndHashCode(callSuper = true)
public class SysRoleMenu extends Model<SysRoleMenu> {

    private static final long serialVersionUID = -9103829087904376249L;

    /**
     * Role ID.
     */
    @Schema(description = "Role ID")
    private Long roleId;

    /**
     * Menu ID.
     */
    @Schema(description = "Menu ID")
    private Long menuId;

}
    