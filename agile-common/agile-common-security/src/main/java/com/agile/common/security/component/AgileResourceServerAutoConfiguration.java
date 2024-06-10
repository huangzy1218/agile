package com.agile.common.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(PermitAllUrlProperties.class)
public class AgileResourceServerAutoConfiguration {

    /**
     * The specific implementation logic of authentication.
     *
     * @return (# pms.xxx)
     */
    @Bean("pms")
    public PermissionService permissionService() {
        return new PermissionService();
    }

    /**
     * The extraction logic of the request token.
     *
     * @param urlProperties list of exposed interfaces
     * @return BearerTokenExtractor
     */
    @Bean
    public AgileBearerTokenExtractor agileBearerTokenExtractor(PermitAllUrlProperties urlProperties) {
        return new AgileBearerTokenExtractor(urlProperties);
    }

    /**
     * Resource server toke introspection processor.
     *
     * @param objectMapper          Jackson output object
     * @param securityMessageSource Custom internationalization processor
     * @return ResourceAuthExceptionEntryPoint
     */
    @Bean
    public ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint(ObjectMapper objectMapper,
                                                                           MessageSource securityMessageSource) {
        return new ResourceAuthExceptionEntryPoint(objectMapper, securityMessageSource);
    }

    /**
     * 资源服务器toke内省处理器
     *
     * @param authorizationService token 存储实现
     * @return TokenIntrospector
     */
    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService) {
        return new PigCustomOpaqueTokenIntrospector(authorizationService);
    }
}
    