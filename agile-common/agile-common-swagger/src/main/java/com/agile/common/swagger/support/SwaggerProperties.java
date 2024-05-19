package com.agile.common.swagger.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Swagger configuration items。
 *
 * @author Huang Z.Y.
 */
@Data
@Component
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    /**
     * Whether to enable swagger.
     */
    private Boolean enabled = true;

    /**
     * Swagger will parse the package path.
     */
    private String basePackage = "";

    /**
     * Swagger will parse url rules.
     **/
    private List<String> basePath = new ArrayList<>();

    /**
     * Url rules that need to be excluded based on basePath.
     **/
    private List<String> excludePath = new ArrayList<>();

    /**
     * Services to be excluded.
     */
    private List<String> ignoreProviders = new ArrayList<>();

    /**
     * Title.
     **/
    private String title = "";

    /**
     * Gateway.
     */
    private String gateway;

    /**
     * Get token.
     */
    private String tokenUrl;

    /**
     * Scope.
     */
    private String scope;

    /**
     * Service forwarding configuration.
     */
    private Map<String, String> services;

}
    