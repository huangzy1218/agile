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
 * Swagger configuration class.
 *
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
public class OpenAPIDefinition extends OpenAPI implements InitializingBean, ApplicationContextAware {

    /**
     * Open API document path.
     */
    @Setter
    private String path;

    private ApplicationContext applicationContext;

    /**
     * Create and return a security scheme for Auth 2.0 cryptographic mode.
     *
     * @param swaggerProperties Swagger configuration properties
     * @return Auth 2.0 Security scheme in password mode
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
        //  Oauth2.0 password
        this.schemaRequirement(HttpHeaders.AUTHORIZATION, this.securityScheme(swaggerProperties));
        // Servers
        List<Server> serverList = new ArrayList<>();
        serverList.add(new Server().url(swaggerProperties.getGateway() + "/" + path));
        this.servers(serverList);
        // Supports parameter tiling
        SpringDocUtils.getConfig().addSimpleTypesForParameterObject(Class.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
    