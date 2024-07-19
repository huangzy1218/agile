package com.agile.admin.service;

import com.agile.admin.api.entity.SysRole;
import com.agile.admin.api.vo.RoleExcelVO;
import com.agile.admin.api.vo.RoleVO;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * Role table Service class.
 *
 * @author Huang Z.Y.
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * Query role information through user ID.
     *
     * @param userId User ID
     */
    List<SysRole> findRolesByUserId(Long userId);

    /**
     * Query role list based on role ID.
     *
     * @param roleIdList Role ID list
     * @param key        Cache key
     */
    List<SysRole> findRolesByRoleIds(List<Long> roleIdList, String key);

    /**
     * Delete a role by role ID.
     *
     * @param ids Role ID list
     * @return {@code true} for success
     */
    Boolean removeRoleByIds(Long[] ids);

    /**
     * According to the role menu list.
     *
     * @param roleVo Role and Menu List
     * @return
     */
    Boolean updateRoleMenus(RoleVO roleVo);

    /**
     * Import roles.
     *
     * @param excelVOList   Role list
     * @param bindingResult Error message list
     * @return {@code true} for success
     */
    R importRole(List<RoleExcelVO> excelVOList, BindingResult bindingResult);

    /**
     * Query all roles
     *
     * @return Role list
     */
    List<RoleExcelVO> listRole();

}

