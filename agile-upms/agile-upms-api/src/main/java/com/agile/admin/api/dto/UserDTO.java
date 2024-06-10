package com.agile.admin.api.dto;

import com.agile.admin.api.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * User dto.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "System user transmit object")
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SysUser {

    /**
     * Role ID.
     */
    @Schema(description = "Role ID")
    private List<Long> role;

    /**
     * Department ID.
     */
    @Schema(description = "Department ID")
    private Long deptId;

    /**
     * Post ID.
     */
    private List<Long> post;

    /**
     * New password.
     */
    @Schema(description = "New password")
    private String newPassword;

}
    