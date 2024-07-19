package com.agile.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.dto.UserDTO;
import com.agile.admin.api.dto.UserInfo;
import com.agile.admin.api.entity.*;
import com.agile.admin.api.util.ParamResolver;
import com.agile.admin.api.vo.UserExcelVO;
import com.agile.admin.api.vo.UserVO;
import com.agile.admin.mapper.SysUserMapper;
import com.agile.admin.mapper.SysUserPostMapper;
import com.agile.admin.mapper.SysUserRoleMapper;
import com.agile.admin.service.*;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.constant.CommonConstants;
import com.agile.common.core.exception.ErrorCodes;
import com.agile.common.core.util.MsgUtils;
import com.agile.common.core.util.R;
import com.agile.common.security.util.SecurityUtils;
import com.agile.plugin.excel.vo.ErrorMessage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User table service implementation class.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final SysMenuService sysMenuService;

    private final SysRoleService sysRoleService;

    private final SysPostService sysPostService;

    private final SysDeptService sysDeptService;

    private final SysUserRoleMapper sysUserRoleMapper;

    private final SysUserPostMapper sysUserPostMapper;

    private final CacheManager cacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
        sysUser.setCreateBy(userDto.getUsername());
        sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        baseMapper.insert(sysUser);
        // Save user position information
        Optional.ofNullable(userDto.getPost()).ifPresent(posts -> {
            posts.stream().map(postId -> {
                SysUserPost userPost = new SysUserPost();
                userPost.setUserId(sysUser.getUserId());
                userPost.setPostId(postId);
                return userPost;
            }).forEach(sysUserPostMapper::insert);
        });

        // If the role is empty, assign the default role
        if (CollUtil.isEmpty(userDto.getRole())) {
            // Get the default role encoding
            String defaultRole = ParamResolver.getStr("USER_DEFAULT_ROLE");
            // Default role
            SysRole sysRole = sysRoleService
                    .getOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getRoleCode, defaultRole));
            userDto.setRole(Collections.singletonList(sysRole.getRoleId()));
        }

        // Insert user role relationship table
        userDto.getRole().stream().map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            return userRole;
        }).forEach(sysUserRoleMapper::insert);
        return Boolean.TRUE;
    }

    /**
     * Query all user information by {@link SysUser}.
     *
     * @param sysUser User
     */
    @Override
    public UserInfo findUserInfo(SysUser sysUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        // Set role ID set
        List<Long> roleIds = sysRoleService.findRolesByUserId(sysUser.getUserId())
                .stream()
                .map(SysRole::getRoleId)
                .collect(Collectors.toList());
        userInfo.setRoles(ArrayUtil.toArray(roleIds, Long.class));

        // Set permission list (menu.permission)
        Set<String> permissions = new HashSet<>();
        roleIds.forEach(roleId -> {
            List<String> permissionList = sysMenuService.findMenuByRoleId(roleId)
                    .stream()
                    .filter(menu -> StrUtil.isNotEmpty(menu.getPermission()))
                    .map(SysMenu::getPermission)
                    .collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return userInfo;
    }

    @Override
    public IPage getUsersWithRolePage(Page page, UserDTO userDTO) {
        return baseMapper.getUserVosPage(page, userDTO);
    }

    @Override
    public UserVO selectUserVoById(Long id) {
        return baseMapper.getUserVoById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserByIds(Long[] ids) {
        // Delete spring cache
        List<SysUser> userList = baseMapper.selectBatchIds(CollUtil.toList(ids));
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        for (SysUser sysUser : userList) {
            // Delete now
            cache.evictIfPresent(sysUser.getUsername());
        }

        sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, CollUtil.toList(ids)));
        this.removeBatchByIds(CollUtil.toList(ids));
        return Boolean.TRUE;
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public R<Boolean> updateUserInfo(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        sysUser.setPhone(userDto.getPhone());
        sysUser.setUserId(SecurityUtils.getUser().getId());
        sysUser.setAvatar(userDto.getAvatar());
        sysUser.setNickname(userDto.getNickname());
        sysUser.setName(userDto.getName());
        sysUser.setEmail(userDto.getEmail());
        return R.ok(this.updateById(sysUser));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public Boolean updateUser(UserDTO userDto) {
        // Update user table information
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(LocalDateTime.now());
        if (StrUtil.isNotBlank(userDto.getPassword())) {
            sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        }
        this.updateById(sysUser);

        // Update user role table
        sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userDto.getUserId()));
        userDto.getRole().stream().map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            return userRole;
        }).forEach(SysUserRole::insert);

        // Update user position list
        sysUserPostMapper.delete(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, userDto.getUserId()));
        userDto.getPost().stream().map(postId -> {
            SysUserPost userPost = new SysUserPost();
            userPost.setUserId(sysUser.getUserId());
            userPost.setPostId(postId);
            return userPost;
        }).forEach(SysUserPost::insert);
        return Boolean.TRUE;
    }

    @Override
    public List<UserExcelVO> listUser(UserDTO userDTO) {
        // Query all user information based on data permissions
        List<UserVO> voList = baseMapper.selectVoList(userDTO);
        // Convert to excel object output
        return voList.stream().map(userVO -> {
            UserExcelVO excelVO = new UserExcelVO();
            BeanUtils.copyProperties(userVO, excelVO);
            String roleNameList = userVO.getRoleList()
                    .stream()
                    .map(SysRole::getRoleName)
                    .collect(Collectors.joining(StrUtil.COMMA));
            excelVO.setRoleNameList(roleNameList);
            String postNameList = userVO.getPostList()
                    .stream()
                    .map(SysPost::getPostName)
                    .collect(Collectors.joining(StrUtil.COMMA));
            excelVO.setPostNameList(postNameList);
            return excelVO;
        }).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public R importUser(List<UserExcelVO> excelVOList, BindingResult bindingResult) {
        // Universal verification to obtain failed data
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();
        List<SysDept> deptList = sysDeptService.list();
        List<SysRole> roleList = sysRoleService.list();
        List<SysPost> postList = sysPostService.list();

        // Perform data insertion operations and assemble UserDTO
        for (UserExcelVO excel : excelVOList) {
            // Personalized verification logic
            List<SysUser> userList = this.list();

            Set<String> errorMsg = new HashSet<>();
            // Verify that the username exists
            boolean exsitUserName = userList.stream()
                    .anyMatch(sysUser -> excel.getUsername().equals(sysUser.getUsername()));

            if (exsitUserName) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING, excel.getUsername()));
            }

            // Determine whether the entered list of department names is legal
            Optional<SysDept> deptOptional = deptList.stream()
                    .filter(dept -> excel.getDeptName().equals(dept.getName()))
                    .findFirst();
            if (!deptOptional.isPresent()) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_DEPT_DEPTNAME_INEXISTENCE, excel.getDeptName()));
            }

            // Determine whether the entered character name list is legal
            List<String> roleNameList = StrUtil.split(excel.getRoleNameList(), StrUtil.COMMA);
            List<SysRole> roleCollList = roleList.stream()
                    .filter(role -> roleNameList.stream().anyMatch(name -> role.getRoleName().equals(name)))
                    .collect(Collectors.toList());

            if (roleCollList.size() != roleNameList.size()) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_ROLE_ROLENAME_INEXISTENCE, excel.getRoleNameList()));
            }

            // Determine whether the entered list of department names is legal
            List<String> postNameList = StrUtil.split(excel.getPostNameList(), StrUtil.COMMA);
            List<SysPost> postCollList = postList.stream()
                    .filter(post -> postNameList.stream().anyMatch(name -> post.getPostName().equals(name)))
                    .collect(Collectors.toList());

            if (postCollList.size() != postNameList.size()) {
                errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_POST_POSTNAME_INEXISTENCE, excel.getPostNameList()));
            }

            // Data legality
            if (CollUtil.isEmpty(errorMsg)) {
                insertExcelUser(excel, deptOptional, roleCollList, postCollList);
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

    /**
     * Insert excel User.
     */
    private void insertExcelUser(UserExcelVO excel, Optional<SysDept> deptOptional, List<SysRole> roleCollList,
                                 List<SysPost> postCollList) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(excel.getUsername());
        userDTO.setPhone(excel.getPhone());
        userDTO.setNickname(excel.getNickname());
        userDTO.setName(excel.getName());
        userDTO.setEmail(excel.getEmail());
        // Batch import initial password is mobile phone number
        userDTO.setPassword(userDTO.getPhone());
        // Query department ID based on department name
        userDTO.setDeptId(deptOptional.get().getDeptId());
        // Insert position title
        List<Long> postIdList = postCollList.stream().map(SysPost::getPostId).collect(Collectors.toList());
        userDTO.setPost(postIdList);
        // Query role ID based on role name
        List<Long> roleIdList = roleCollList.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        userDTO.setRole(roleIdList);
        // Insert user
        this.saveUser(userDTO);
    }

    /**
     * Register a user and assign a default role to the user.
     *
     * @param userDto User information
     * @return {@code true} for success
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> registerUser(UserDTO userDto) {
        // Determine whether the username exists
        SysUser sysUser = this.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, userDto.getUsername()));
        if (sysUser != null) {
            String message = MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING, userDto.getUsername());
            return R.failed(message);
        }
        return R.ok(saveUser(userDto));
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#username")
    public R<Boolean> lockUser(String username) {
        SysUser sysUser = baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));

        if (Objects.nonNull(sysUser)) {
            sysUser.setLockFlag(CommonConstants.STATUS_LOCK);
            baseMapper.updateById(sysUser);
        }
        return R.ok();
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public R changePassword(UserDTO userDto) {
        SysUser sysUser = baseMapper.selectById(SecurityUtils.getUser().getId());
        if (Objects.isNull(sysUser)) {
            return R.failed("User does not exist");
        }

        if (StrUtil.isEmpty(userDto.getPassword())) {
            return R.failed("The original password cannot be empty");
        }

        if (!ENCODER.matches(userDto.getPassword(), sysUser.getPassword())) {
            log.info("The original password is wrong and the modification of personal information failed: {}", userDto.getUsername());
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_UPDATE_PASSWORDERROR));
        }

        if (StrUtil.isEmpty(userDto.getNewPassword())) {
            return R.failed("New password cannot be empty");
        }
        String password = ENCODER.encode(userDto.getNewPassword());

        this.update(Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getPassword, password)
                .eq(SysUser::getUserId, sysUser.getUserId()));
        return R.ok();
    }

    @Override
    public R checkPassword(String password) {
        SysUser sysUser = baseMapper.selectById(SecurityUtils.getUser().getId());

        if (!ENCODER.matches(password, sysUser.getPassword())) {
            log.info("The original password is wrong");
            return R.failed("Incorrect password");
        } else {
            return R.ok();
        }
    }

}

