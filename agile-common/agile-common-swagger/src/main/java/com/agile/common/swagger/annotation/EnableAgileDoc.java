package com.agile.common.swagger.annotation;

import com.agile.common.core.factory.YamlPropertySourceFactory;
import com.agile.common.swagger.config.OpenAPIDefinitionImportSelector;
import com.agile.common.swagger.support.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.lang.annotation.*;

/**
 * Open the Spring Doc.
 *
 * @author Huang Z.Y.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(SwaggerProperties.class)
@Import(OpenAPIDefinitionImportSelector.class)
@PropertySource(value = "classpath:openapi-config.yaml", factory = YamlPropertySourceFactory.class)
public @interface EnableAgileDoc {

    /**
     * Gateway route prefix
     */
    String value();

    /**
     * Is a microservices architecture or not.
     */
    boolean isMicro() default true;
}
