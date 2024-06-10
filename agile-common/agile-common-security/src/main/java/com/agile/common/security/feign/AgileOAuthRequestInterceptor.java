package com.agile.common.security.feign;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.core.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import java.util.Collection;

/**
 * Oauth2 feign token passing.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@RequiredArgsConstructor
public class AgileOAuthRequestInterceptor implements RequestInterceptor {

    private final BearerTokenResolver tokenResolver;

    /**
     * Create a template with the header of provided name and extracted extract </br>
     * Restore the request token based on authentication.
     *
     * @param template RequestTemplate
     */
    @Override
    public void apply(RequestTemplate template) {
        Collection<String> fromHeader = template.headers().get(SecurityConstants.FROM);
        // Requests with from are skipped directly
        if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)) {
            return;
        }

        // Non-web requests are skipped
        if (!WebUtils.getRequest().isPresent()) {
            return;
        }
        HttpServletRequest request = WebUtils.getRequest().get();
        // Avoid query tokens for request parameters that cannot be passed
        String token = tokenResolver.resolve(request);
        if (StrUtil.isBlank(token)) {
            return;
        }
        // Authorization: Bearer token
        template.header(HttpHeaders.AUTHORIZATION,
                String.format("%s %s", OAuth2AccessToken.TokenType.BEARER.getValue(), token));

    }

}
    