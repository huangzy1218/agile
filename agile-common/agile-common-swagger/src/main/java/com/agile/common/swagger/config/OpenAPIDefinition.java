package com.agile.common.swagger.config;

import com.agile.common.swagger.support.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger配置。
 *
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
public class OpenAPIDefinition extends OpenAPI implements InitializingBean, ApplicationContextAware {

    /**
     * OpenAPI文档路径。
     */
    @Setter
    private String path;

    private ApplicationContext applicationContext;

    /**
     * 创建并返回OAuth2.0密码模式的安全方案。
     *
     * @param swaggerProperties Swagger配置属性
     * @return OAuth2.0密码模式的安全方案
     */
    private SecurityScheme securityScheme(SwaggerProperties swaggerProperties) {
        OAuthFlow clientCredential = new OAuthFlow();
        clientCredential.setTokenUrl(swaggerProperties.getTokenUrl());
        clientCredential.setScopes(new Scopes().addString(swaggerProperties.getScope(), swaggerProperties.getScope()));
        OAuthFlows oauthFlows = new OAuthFlows();
        oauthFlows.password(clientCredential);
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(SecurityScheme.Type.OAUTH2);
        securityScheme.setFlows(oauthFlows);
        return securityScheme;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SwaggerProperties swaggerProperties = applicationContext.getBean(SwaggerProperties.class);
        this.info(new Info().title(swaggerProperties.getTitle()));
        //  oauth2.0密码模式
        this.schemaRequirement(HttpHeaders.AUTHORIZATION, this.securityScheme(swaggerProperties));
        // 服务器信息
        List<Server> serverList = new ArrayList<>();
        serverList.add(new Server().url(swaggerProperties.getGateway() + "/" + path));
        this.servers(serverList);
        // 支持参数平铺
        SpringDocUtils.getConfig().addSimpleTypesForParameterObject(Class.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
    