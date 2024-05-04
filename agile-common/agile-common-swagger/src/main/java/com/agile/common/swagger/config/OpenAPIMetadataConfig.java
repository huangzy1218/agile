package com.agile.common.swagger.config;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 配置Open API元数据。
 *
 * @author Huang Z.Y.
 */
public class OpenAPIMetadataConfig implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * OpenAPI元数据的路径。
     */
    @Setter
    private String path;

    /**
     * 在容器设置完所有Bean属性后调用。
     *
     * @throws Exception 如果在初始化期间发生错误
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
    