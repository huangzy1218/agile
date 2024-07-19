package com.agile.admin.service;

import com.agile.admin.api.dto.UserDTO;
import com.agile.admin.api.dto.UserInfo;
import com.agile.admin.api.entity.SysUser;
import com.agile.admin.api.vo.UserExcelVO;
import com.agile.admin.api.vo.UserVO;
import com.agile.common.core.util.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * User table service class.
 *
 * @author Huang Z.Y.
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * Query user information.
     *
     * @param sysUser User
     * @return User information
     */
    UserInfo findUserInfo(SysUser sysUser);

    /**
     * Query user information in pages (including role information)
     *
     * @param page    Pagination object
     * @param userDTO Parameter list
     * @return
     */
    IPage getUsersWithRolePage(Page page, UserDTO userDTO);

    /**
     * Delete user.
     *
     * @param ids User
     * @return {@code true} for success
     */
    Boolean deleteUserByIds(Long[] ids);

    /**
     * Update basic information of current user.
     *
     * @param userDto User information
     * @return {@code true} for success
     */
    R<Boolean> updateUserInfo(UserDTO userDto);

    /**
     * Update specified user information.
     *
     * @param userDto User information
     * @return {@code true} for success
     */
    Boolean updateUser(UserDTO userDto);

    /**
     * Query user information by ID.
     *
     * @param id User ID
     * @return User information
     */
    UserVO selectUserVoById(Long id);

    /**
     * Save user information
     *
     * @param userDto User DTO
     * @return {@code true} for success
     */
    Boolean saveUser(UserDTO userDto);

    /**
     * Query all users.
     *
     * @param userDTO Query conditions
     * @return Use list
     */
    List<UserExcelVO> listUser(UserDTO userDTO);

    /**
     * Excel import users
     *
     * @param excelVOList   Excel list data
     * @param bindingResult Error data
     * @return {@code true} for success
     */
    R importUser(List<UserExcelVO> excelVOList, BindingResult bindingResult);

    /**
     * Registered user.
     *
     * @param userDto User information
     * @return {@code true} for success
     */
    R<Boolean> registerUser(UserDTO userDto);

    /**
     * Lock user.
     *
     * @param username Username
     * @return {@code true} for success
     */
    R<Boolean> lockUser(String username);

    /**
     * Change Password.
     *
     * @param userDto User information
     */
    R changePassword(UserDTO userDto);

    /**
     * Verify password.
     *
     * @param password Password plain text
     */
    R checkPassword(String password);

}

