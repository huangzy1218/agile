package com.agile.common.swagger.config;

import com.agile.common.swagger.annotation.EnableAgileDoc;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * 根据@EnableDoc注解的配置信息注册OpenAPI相关的Bean定义。
 *
 * @author Huang Z.Y.
 */
public class OpenAPIDefinitionImportSelector implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        // 获取@EnableDoc注解的配置信息
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnableAgileDoc.class.getName(), true);
        Object value = annotationAttributes.get("value");
        if (Objects.isNull(value)) {
            return;
        }

        // 注册OpenAPIDefinition类的Bean定义
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(OpenAPIDefinition.class);
        definition.addPropertyValue("path", value);
        definition.setPrimary(true);

        registry.registerBeanDefinition("openAPIDefinition", definition.getBeanDefinition());

        // 如果是微服务架构则，引入了服务发现声明相关的元数据配置
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
    