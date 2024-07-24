package com.agile.daemon.quartz.config;

import com.agile.common.core.factory.YamlPropertySourceFactory;
import org.quartz.Calendar;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Agile quartz configuration.
 *
 * @author Huang Z.Y.
 */
@EnableAsync
@Configuration
@PropertySource(value = "classpath:quartz-config.yml", factory = YamlPropertySourceFactory.class)
@ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class})
@EnableConfigurationProperties({QuartzProperties.class})
public class AgileQuartzConfig {

    private final QuartzProperties properties;

    private final List<SchedulerFactoryBeanCustomizer> customizers;

    private final JobDetail[] jobDetails;

    private final Map<String, Calendar> calendars;

    private final Trigger[] triggers;

    private final ApplicationContext applicationContext;

    public AgileQuartzConfig(QuartzProperties properties,
                             ObjectProvider<List<SchedulerFactoryBeanCustomizer>> customizers, ObjectProvider<JobDetail[]> jobDetails,
                             ObjectProvider<Map<String, Calendar>> calendars, ObjectProvider<Trigger[]> triggers,
                             ApplicationContext applicationContext) {
        this.properties = properties;
        this.customizers = customizers.getIfAvailable();
        this.jobDetails = jobDetails.getIfAvailable();
        this.calendars = calendars.getIfAvailable();
        this.triggers = triggers.getIfAvailable();
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean
                .setJobFactory(new AutowireCapableBeanJobFactory(this.applicationContext.getAutowireCapableBeanFactory()));
        if (!this.properties.getProperties().isEmpty()) {
            schedulerFactoryBean.setQuartzProperties(this.asProperties(this.properties.getProperties()));
        }

        if (this.jobDetails != null && this.jobDetails.length > 0) {
            schedulerFactoryBean.setJobDetails(this.jobDetails);
        }

        if (this.calendars != null && !this.calendars.isEmpty()) {
            schedulerFactoryBean.setCalendars(this.calendars);
        }

        if (this.triggers != null && this.triggers.length > 0) {
            schedulerFactoryBean.setTriggers(this.triggers);
        }

        this.customize(schedulerFactoryBean);
        return schedulerFactoryBean;
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }

    private void customize(SchedulerFactoryBean schedulerFactoryBean) {
        if (this.customizers != null) {

            for (SchedulerFactoryBeanCustomizer customizer : this.customizers) {
                customizer.customize(schedulerFactoryBean);
            }
        }

    }

    /**
     * Get an instance of Scheduler through SchedulerFactoryBean.
     *
     * @return Scheduler
     */
    @Bean
    public Scheduler scheduler() {
        return quartzScheduler().getScheduler();
    }

}
