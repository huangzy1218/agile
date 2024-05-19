package com.agile.common.swagger.config;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Open API metadata configuration class.
 *
 * @author Huang Z.Y.
 * @see OpenAPIDefinitionImportSelector
 * @see com.agile.common.swagger.annotation.EnableAgileDoc
 */
public class OpenAPIMetadataConfig implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * Open API metadata path.
     */
    @Setter
    private String path;

    /**
     * This method retrieves all bean names for the type ServiceInstance from the application context.<br/>
     * It retrieves a bean of type ServiceInstance from the application context
     * and adds metadata with the key "spring-doc" and the provided path value.
     *
     * @throws Exception If an error occurs during initialization
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(ServiceInstance.class);

        if (beanNamesForType.length == 0) {
            return;
        }

        ServiceInstance serviceInstance = applicationContext.getBean(ServiceInstance.class);
        serviceInstance.getMetadata().put("spring-doc", path);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
    