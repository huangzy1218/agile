package com.agile.common.swagger.config;

import com.agile.common.swagger.annotation.EnableAgileDoc;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * Register the Bean definitions associated with the Open API according to the configuration information in the
 * {@link EnableAgileDoc @EnableAgileDoc}
 * annotation.
 *
 * @author Huang Z.Y.
 */
public class OpenAPIDefinitionImportSelector implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        // Get the configuration information for the @EnableAgileDoc annotation
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnableAgileDoc.class.getName(), true);
        Object value = Objects.requireNonNull(annotationAttributes).get("value");
        if (Objects.isNull(value)) {
            return;
        }

        // Register the Bean Definition for the Open API Definition class
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(OpenAPIDefinition.class);
        definition.addPropertyValue("path", value);
        definition.setPrimary(true);

        registry.registerBeanDefinition("openAPIDefinition", definition.getBeanDefinition());

        // In the case of a microservices architecture,
        // metadata configuration related to service discovery declarations is introduced
        Object isMicro = annotationAttributes.getOrDefault("isMicro", true);
        if (isMicro.equals(false)) {
            return;
        }

        BeanDefinitionBuilder openAPIMetadata = BeanDefinitionBuilder
                .genericBeanDefinition(OpenAPIMetadataConfig.class);
        openAPIMetadata.addPropertyValue("path", value);
        registry.registerBeanDefinition("openAPIMetadata", openAPIMetadata.getBeanDefinition());
    }
    
}
    