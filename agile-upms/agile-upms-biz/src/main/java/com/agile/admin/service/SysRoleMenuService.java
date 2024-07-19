package com.agile.admin.service;

import com.agile.admin.api.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Role menu table Service class.
 *
 * @author Huang Z.Y.
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * Update role menu.
     *
     * @param roleId  Role ID
     * @param menuIds A string of menu IDs, separated by commas.
     */
    Boolean saveRoleMenus(Long roleId, String menuIds);

}

