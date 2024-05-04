package com.agile.common.core.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Service
@Lazy(value = false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    /**
     * Gets the Application Context stored in a static variable.
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Implement the {@link ApplicationContextAware} interface and inject Context into a static variable.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }

    /**
     * Gets the Bean from the static variable applicationContext and automatically
     * transitions it to the type of the assigned object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * Gets the Bean from the static variable applicationContext and automatically
     * transitions it to the type of the assigned object.
     */
    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    /**
     * Clear the {@link ApplicationContext} to null.
     */
    public static void clearHolder() {
        if (log.isDebugEnabled()) {
            log.debug("clear the ApplicationContext in the Spring Context Holder:" + applicationContext);
        }
        applicationContext = null;
    }

    /**
     * Publish event.
     *
     * @param event application event
     */
    public static void publishEvent(ApplicationEvent event) {
        if (applicationContext == null) {
            return;
        }
        applicationContext.publishEvent(event);
    }

    /**
     * Implements the {@link DisposableBean} interface to clean up
     * static variables when Context closes.
     */
    @Override
    @SneakyThrows
    public void destroy() {
        SpringContextHolder.clearHolder();
    }
}
    