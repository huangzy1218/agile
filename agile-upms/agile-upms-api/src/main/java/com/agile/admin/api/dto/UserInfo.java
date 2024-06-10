package com.agile.admin.api.dto;

import com.agile.admin.api.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Encapsulate user information and permission information.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "User info")
public class UserInfo {

    /**
     * User basic information.
     */
    @Schema(description = "User basic information")
    private SysUser sysUser;

    /**
     * Permission identification set.
     */
    @Schema(description = "Permission identification se")
    private String[] permissions;

    /**
     * Role set.
     */
    @Schema(description = " Role set")
    private Long[] roles;

}
    