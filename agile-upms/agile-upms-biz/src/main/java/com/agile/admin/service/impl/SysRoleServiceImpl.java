package com.agile.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.agile.admin.api.entity.SysRole;
import com.agile.admin.api.entity.SysRoleMenu;
import com.agile.admin.api.vo.RoleExcelVO;
import com.agile.admin.api.vo.RoleVO;
import com.agile.admin.mapper.SysRoleMapper;
import com.agile.admin.service.SysRoleMenuService;
import com.agile.admin.service.SysRoleService;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.exception.ErrorCodes;
import com.agile.common.core.util.MsgUtils;
import com.agile.common.core.util.R;
import com.agile.plugin.excel.vo.ErrorMessage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Huang Z.Y.
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private SysRoleMenuService roleMenuService;

    /**
     * Query role information through user ID.
     *
     * @param userId User ID
     */
    @Override
    public List findRolesByUserId(Long userId) {
        return baseMapper.listRolesByUserId(userId);
    }

    /**
     * Query the role list based on the role ID, pay attention to cache deletion.
     *
     * @param roleIdList Role ID list
     * @param key        Cache key
     * @return
     */
    @Override
    @Cacheable(value = CacheConstants.ROLE_DETAILS, key = "#key", unless = "#result.isEmpty()")
    public List<SysRole> findRolesByRoleIds(List<Long> roleIdList, String key) {
        return baseMapper.selectBatchIds(roleIdList);
    }

    /**
     * Delete the role through the role ID and clear the role menu cache.
     *
     * @param ids ID list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeRoleByIds(Long[] ids) {
        roleMenuService
                .remove(Wrappers.<SysRoleMenu>update().lambda().in(SysRoleMenu::getRoleId, CollUtil.toList(ids)));
        return this.removeBatchByIds(CollUtil.toList(ids));
    }

    @Override
    public Boolean updateRoleMenus(RoleVO roleVo) {
        return roleMenuService.saveRoleMenus(roleVo.getRoleId(), roleVo.getMenuIds());
    }

    @Override
    public R importRole(List<RoleExcelVO> excelVOList, BindingResult bindingResult) {
        // Universal verification to obtain failed data
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

        // Personalized verification logic
        List<SysRole> roleList = this.list();

        // Perform data insertion operations and assemble RoleDto
        for (RoleExcelVO excel : excelVOList) {
            Set<String> errorMsg = new HashSet<>();
            // Check whether the role name or role code exists
            boolean existRole = roleList.stream()
                    .anyMatch(sysRole -> excel.getRoleName().equals(sysRole.getRoleName())
                            || excel.getRoleCode().equals(sysRole.getRoleCode()));

            if (existRole) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_ROLE_NAMEORCODE_EXISTING, excel.getRoleName(),
                        excel.getRoleCode()));
            }

            // Data legality
            if (CollUtil.isEmpty(errorMsg)) {
                insertExcelRole(excel);
            } else {
                // Data is illegal
                errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
            }
        }
        if (CollUtil.isNotEmpty(errorMessageList)) {
            return R.failed(errorMessageList);
        }
        return R.ok();
    }

    @Override
    public List<RoleExcelVO> listRole() {
        List<SysRole> roleList = this.list(Wrappers.emptyWrapper());
        // Convert to execl object output
        return roleList.stream().map(role -> {
            RoleExcelVO roleExcelVO = new RoleExcelVO();
            BeanUtil.copyProperties(role, roleExcelVO);
            return roleExcelVO;
        }).collect(Collectors.toList());
    }

    private void insertExcelRole(RoleExcelVO excel) {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName(excel.getRoleName());
        sysRole.setRoleDesc(excel.getRoleDesc());
        sysRole.setRoleCode(excel.getRoleCode());
        this.save(sysRole);
    }

}

