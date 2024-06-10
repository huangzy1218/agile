package com.agile.common.security.service;

import com.agile.admin.api.dto.UserDTO;
import com.agile.admin.api.dto.UserInfo;
import com.agile.admin.api.feign.RemoteUserService;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * User details implementation.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Primary
@RequiredArgsConstructor
public class AgileUserDetailsServiceImpl implements AgileUserDetailsService {

    private final RemoteUserService remoteUserService;

    private final CacheManager cacheManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (cache != null && cache.get(username) != null) {
            return (AgileUser) cache.get(username).get();
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        R<UserInfo> result = remoteUserService.info(userDTO, SecurityConstants.FROM_IN);
        UserDetails userDetails = getUserDetails(result);
        if (cache != null) {
            cache.put(username, userDetails);
        }
        return userDetails;
    }

    /**
     * Highest priority.
     */
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

}
    