package com.agile.admin.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User role.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "User role")
@EqualsAndHashCode(callSuper = true)
public class SysUserRole extends Model<SysUserRole> {

    private static final long serialVersionUID = 1L;

    /**
     * User ID.
     */
    @Schema(description = "User ID")
    private Long userId;

    /**
     * Role ID.
     */
    @Schema(description = "Role ID")
    private Long roleId;

}
    