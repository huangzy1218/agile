package com.agile.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.dto.UserDTO;
import com.agile.admin.api.entity.SysUser;
import com.agile.admin.api.vo.UserExcelVO;
import com.agile.admin.service.SysUserService;
import com.agile.common.core.constant.CommonConstants;
import com.agile.common.core.exception.ErrorCodes;
import com.agile.common.core.util.MsgUtils;
import com.agile.common.core.util.R;
import com.agile.common.log.annotation.SysLog;
import com.agile.common.security.annotation.Inner;
import com.agile.common.security.util.SecurityUtils;
import com.agile.plugin.excel.annotation.RequestExcel;
import com.agile.plugin.excel.annotation.ResponseExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User management module.
 *
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Tag(description = "user", name = "User management module")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysUserController {

    private final SysUserService userService;

    /**
     * Get all information of a specified user.
     *
     * @return User information
     */
    @Inner
    @GetMapping(value = {"/info/query"})
    public R info(@RequestParam(required = false) String username, @RequestParam(required = false) String phone) {
        SysUser user = userService.getOne(Wrappers.<SysUser>query()
                .lambda()
                .eq(StrUtil.isNotBlank(username), SysUser::getUsername, username)
                .eq(StrUtil.isNotBlank(phone), SysUser::getPhone, phone));
        if (user == null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERINFO_EMPTY, username));
        }
        return R.ok(userService.findUserInfo(user));
    }

    /**
     * Get all information of current user.
     *
     * @return User information
     */
    @GetMapping(value = {"/info"})
    public R info() {
        String username = SecurityUtils.getUser().getUsername();
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_QUERY_ERROR));
        }
        return R.ok(userService.findUserInfo(user));
    }

    /**
     * Query user information by ID.
     *
     * @param id ID
     * @return User information
     */
    @GetMapping("/details/{id}")
    public R user(@PathVariable Long id) {
        return R.ok(userService.selectUserVoById(id));
    }

    /**
     * Query user information.
     *
     * @param query Query information
     * @return Returns username if not empty
     */
    @Inner(value = false)
    @GetMapping("/details")
    public R getDetails(@ParameterObject SysUser query) {
        SysUser sysUser = userService.getOne(Wrappers.query(query), false);
        return R.ok(sysUser == null ? null : CommonConstants.SUCCESS);
    }

    /**
     * Delete user information.
     *
     * @param ids ID
     * @return R
     */
    @SysLog("删除用户信息")
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('sys_user_del')")
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    public R userDel(@RequestBody Long[] ids) {
        return R.ok(userService.deleteUserByIds(ids));
    }

    /**
     * Add user.
     *
     * @param userDto User information
     * @return {@code true} for success
     */
    @SysLog("Add user")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_user_add')")
    public R user(@RequestBody UserDTO userDto) {
        return R.ok(userService.saveUser(userDto));
    }

    /**
     * Update user information.
     *
     * @param userDto User information
     */
    @SysLog("Update user information")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_user_edit')")
    public R updateUser(@Valid @RequestBody UserDTO userDto) {
        return R.ok(userService.updateUser(userDto));
    }

    /**
     * Query users by page.
     *
     * @param page    Parameter set
     * @param userDTO Query parameter list
     * @return 用户集合
     */
    @GetMapping("/page")
    public R getUserPage(@ParameterObject Page page, @ParameterObject UserDTO userDTO) {
        return R.ok(userService.getUsersWithRolePage(page, userDTO));
    }

    /**
     * Modify personal information.
     *
     * @param userDto User information DTO
     * @return {@code true} for success
     */
    @SysLog("Modify personal information")
    @PutMapping("/edit")
    public R updateUserInfo(@Valid @RequestBody UserDTO userDto) {
        return userService.updateUserInfo(userDto);
    }

    /**
     * Export excel table.
     *
     * @param userDTO Query conditions
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('sys_user_export')")
    public List export(UserDTO userDTO) {
        return userService.listUser(userDTO);
    }

    /**
     * Import users.
     *
     * @param excelVOList   User list
     * @param bindingResult Error message list
     */
    @PostMapping("/import")
    @PreAuthorize("@pms.hasPermission('sys_user_export')")
    public R importUser(@RequestExcel List<UserExcelVO> excelVOList, BindingResult bindingResult) {
        return userService.importUser(excelVOList, bindingResult);
    }

    /**
     * Lock specified user.
     *
     * @param username Username
     * @return R
     */
    @PutMapping("/lock/{username}")
    public R lockUser(@PathVariable String username) {
        return userService.lockUser(username);
    }

    @PutMapping("/password")
    public R password(@RequestBody UserDTO userDto) {
        String username = SecurityUtils.getUser().getUsername();
        userDto.setUsername(username);
        return userService.changePassword(userDto);
    }

    @PostMapping("/check")
    public R check(String password) {
        return userService.checkPassword(password);
    }

}

