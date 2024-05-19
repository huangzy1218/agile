package com.agile.common.log.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Automatically maintains the Spring Boot Admin Logger Viewer by injecting logging.file as an environment variable.
 *
 * @author Huang Z.Y.
 */
public class ApplicationLoggerInitializer implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String appName = environment.getProperty("spring.application.name");
        String logBase = environment.getProperty("LOGGING_PATH", "logs");

        // SpringBoot admin directly load log
        System.setProperty("logging.file.name", String.format("%s/%s/debug.log", logBase, appName));

        // Prevent sentinel 1.8.4+ heartbeat logs from being too large
        System.setProperty("csp.sentinel.log.level", "OFF");
    }

    @Override
    public int getOrder() {
        // lowest priority
        return Ordered.LOWEST_PRECEDENCE;
    }
}
    