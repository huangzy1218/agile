package com.agile.common.security.util;

import cn.hutool.core.util.StrUtil;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.security.service.AgileUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Security tools.
 *
 * @author Huang Z.Y.
 */
@UtilityClass
public class SecurityUtils {

    /**
     * Get authentication.
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Get user.
     */
    public AgileUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof AgileUser) {
            return (AgileUser) principal;
        }
        return null;
    }

    /**
     * Get user.
     */
    public AgileUser getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getUser(authentication);
    }

    /**
     * Get user role information.
     *
     * @return Role set
     */
    public List<Long> getRoles() {
        Authentication authentication = getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<Long> roleIds = new ArrayList<>();
        authorities.stream()
                .filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
                .forEach(granted -> {
                    String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
                    roleIds.add(Long.parseLong(id));
                });
        return roleIds;
    }

}

