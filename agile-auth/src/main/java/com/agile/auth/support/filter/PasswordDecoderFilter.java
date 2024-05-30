package com.agile.auth.support.filter;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.core.servlet.RepeatBodyRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Map;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordDecoderFilter extends OncePerRequestFilter {

    private final AuthSecurityConfigProperties authSecurityConfigProperties;

    private static final String PASSWORD = "password";

    private static final String KEY_ALGORITHM = "AES";

    static {
        // Disable hutool Forcibly disables the dependency of the Bouncy Castle library
        SecureUtil.disableBouncyCastle();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // It's not a login request, just go down
        if (!StrUtil.containsAnyIgnoreCase(request.getRequestURI(), SecurityConstants.OAUTH_TOKEN_URL)) {
            chain.doFilter(request, response);
            return;
        }

        // Transform a request stream into one that can be read multiple times
        RepeatBodyRequestWrapper requestWrapper = new RepeatBodyRequestWrapper(request);
        Map<String, String[]> parameterMap = requestWrapper.getParameterMap();

        // Build the front-end corresponding decryption AES factor
        AES aes = new AES(Mode.CFB, Padding.NoPadding,
                new SecretKeySpec(authSecurityConfigProperties.getEncodeKey().getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(authSecurityConfigProperties.getEncodeKey().getBytes()));

        parameterMap.forEach((k, v) -> {
            String[] values = parameterMap.get(k);
            // Get the parameter equals to "password"
            if (!PASSWORD.equals(k) || ArrayUtil.isEmpty(values)) {
                return;
            }

            // Decode password
            String decryptPassword = aes.decryptStr(values[0]);
            // Replaces the original parameter value with decode password
            parameterMap.put(k, new String[]{decryptPassword});
        });
        chain.doFilter(requestWrapper, response);
    }

}
    