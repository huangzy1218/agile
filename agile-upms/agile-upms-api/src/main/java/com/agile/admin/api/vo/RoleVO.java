package com.agile.admin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Front-end role display object.
 *
 * @author Huang Z.Y.
 */
@Data
@Schema(description = "Front-end role display object")
public class RoleVO {

    /**
     * Role ID.
     */
    private Long roleId;

    /**
     * Menu list.
     */
    private String menuIds;

}

    