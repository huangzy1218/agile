package com.agile.daemon.quartz.config;

import org.quartz.JobKey;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.util.Assert;

/**
 * Use Spring's autowiring function (Autowiring) to manage the dependency injection and
 * initialization process of Job instances (default is reflection).
 *
 * @author Huang Z.Y.
 */
public class AutowireCapableBeanJobFactory extends SpringBeanJobFactory {

    private final AutowireCapableBeanFactory beanFactory;

    AutowireCapableBeanJobFactory(AutowireCapableBeanFactory beanFactory) {
        Assert.notNull(beanFactory, "Bean factory must not be null");
        this.beanFactory = beanFactory;
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        this.beanFactory.autowireBean(jobInstance);

        // BeanName must be injected here, otherwise sentinel will report an error
        JobKey jobKey = bundle.getTrigger().getJobKey();
        String beanName = jobKey + jobKey.getName();
        this.beanFactory.initializeBean(jobInstance, beanName);
        return jobInstance;
    }

}
