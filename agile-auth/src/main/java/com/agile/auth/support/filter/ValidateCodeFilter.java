package com.agile.auth.support.filter;

import cn.hutool.core.util.StrUtil;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.core.exception.ValidateCodeException;
import com.agile.common.core.util.SpringContextHolder;
import com.agile.common.core.util.WebUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Front-end verification code process.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final AuthSecurityConfigProperties authSecurityConfigProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUrl = request.getServletPath();

        // Not login URL requests are skipped directly
        if (!SecurityConstants.OAUTH_TOKEN_URL.equals(requestUrl)) {
            filterChain.doFilter(request, response);
            return;
        }

        // If the login URL is a request to refresh the token (grant_type = refresh_token), go straight down
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Skip the verification code on the client
        boolean isIgnoreClient = authSecurityConfigProperties.getIgnoreClients().contains(WebUtils.getClientId());
        if (isIgnoreClient) {
            filterChain.doFilter(request, response);
            return;
        }

        // Verify the verification code: Enable the verification code (picture) or SMS
        try {
            checkCode();
            filterChain.doFilter(request, response);
        } catch (ValidateCodeException validateCodeException) {
            throw new OAuth2AuthenticationException(validateCodeException.getMessage());
        }
    }

    /**
     * Validates the verification code from the HTTP request.
     */
    private void checkCode() throws ValidateCodeException {
        // Retrieve the current HTTP request
        Optional<HttpServletRequest> request = WebUtils.getRequest();
        String code = request.get().getParameter("code");

        if (StrUtil.isBlank(code)) {
            throw new ValidateCodeException("The verification code cannot be empty");
        }

        // Serve as a key suffix used to retrieve the stored verification code from the Redis
        String randomStr = request.get().getParameter("randomStr");

        // Verification codes are sent via SMS to mobile numbers
        String mobile = request.get().getParameter("mobile");
        if (StrUtil.isNotBlank(mobile)) {
            randomStr = mobile;
        }

        // Construct the Redis key using the default prefix and the random string
        String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;
        RedisTemplate<String, String> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
        // Check if the key exists in Redis
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            throw new ValidateCodeException("The verification code is invalid");
        }

        Object codeObj = redisTemplate.opsForValue().get(key);

        if (codeObj == null) {
            throw new ValidateCodeException("The verification code is invalid");
        }

        String saveCode = codeObj.toString();
        if (StrUtil.isBlank(saveCode)) {
            redisTemplate.delete(key);
            throw new ValidateCodeException("The verification code is invalid");
        }

        if (!StrUtil.equals(saveCode, code)) {
            redisTemplate.delete(key);
            throw new ValidateCodeException("The verification code is invalid");
        }

        // Delete the key from Redis after successful validation
        redisTemplate.delete(key);
    }

}
    