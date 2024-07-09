package com.agile.common.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Configure the asynchronous task executor.<br/>
 * {@link AsyncConfigurer} used to configure parameters and thread pools associated with asynchronous method execution.
 *
 * @author Huang Z.Y.
 */
public class TaskExecutorConfiguration implements AsyncConfigurer {

    /**
     * The number of cores obtained on the current machine may not be accurate.
     * (CPU intensive or IO intensive).
     */
    public static final int cpuNum = Runtime.getRuntime().availableProcessors();

    /**
     * If property does not exist, use the default value ""
     */
    @Value("${thread.pool.corePoolSize:}")
    private Optional<Integer> corePoolSize;

    @Value("${thread.pool.maxPoolSize:}")
    private Optional<Integer> maxPoolSize;

    @Value("${thread.pool.queueCapacity:}")
    private Optional<Integer> queueCapacity;

    @Value("${thread.pool.awaitTerminationSeconds:}")
    private Optional<Integer> awaitTerminationSeconds;

    @Override
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // Core thread size number of cpus in the default area
        taskExecutor.setCorePoolSize(corePoolSize.orElse(cpuNum));
        // Maximum thread size default area CPU 2 number of cpus
        taskExecutor.setMaxPoolSize(maxPoolSize.orElse(cpuNum * 2));
        // Maximum queue capacity
        taskExecutor.setQueueCapacity(queueCapacity.orElse(500));
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(awaitTerminationSeconds.orElse(60));
        taskExecutor.setThreadNamePrefix("AGILE-Thread-");
        taskExecutor.initialize();
        return taskExecutor;
    }

}
    