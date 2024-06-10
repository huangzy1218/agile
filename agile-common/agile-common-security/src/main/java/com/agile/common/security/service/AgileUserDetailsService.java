package com.agile.common.security.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.admin.api.dto.UserInfo;
import com.agile.admin.api.entity.SysUser;
import com.agile.common.core.constant.CommonConstants;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.core.util.R;
import com.agile.common.core.util.RetOps;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Huang Z.Y.
 */
public interface AgileUserDetailsService extends UserDetailsService, Ordered {

    /**
     * Whether the client verification is supported.
     *
     * @param clientId Target client
     * @return {@code true} for supported
     */
    default boolean support(String clientId, String grantType) {
        return true;
    }

    @Override
    default int getOrder() {
        return 0;
    }

    /**
     * Build {@link  UserDetails}.
     *
     * @param result User information
     * @return UserDetails
     */
    default UserDetails getUserDetails(R<UserInfo> result) {
        UserInfo info = RetOps.of(result).getData().orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        Set<String> dbAuthsSet = new HashSet<>();

        if (ArrayUtil.isNotEmpty(info.getRoles())) {
            // 获取角色
            Arrays.stream(info.getRoles()).forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
            // 获取资源
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

        }

        Collection<GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(dbAuthsSet.toArray(new String[0]));
        SysUser user = info.getSysUser();

        // Construct security user
        return new AgileUser(user.getUserId(), user.getDeptId(), user.getUsername(),
                SecurityConstants.BCRYPT + user.getPassword(), user.getPhone(), true, true, true,
                StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL), authorities);
    }

    /**
     * Query by user entity.
     *
     * @param agileUser User
     */
    default UserDetails loadUserByUser(AgileUser agileUser) {
        return this.loadUserByUsername(agileUser.getUsername());
    }

}
