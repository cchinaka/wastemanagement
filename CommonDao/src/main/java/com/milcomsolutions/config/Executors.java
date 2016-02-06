package com.milcomsolutions.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class Executors {

	@Value("${processor.core.size:5}")
	private int corePoolSize;
	

	@Value("${processor.max.size:30}")
	private int maxPoolSize;
	
    @Bean(name = "processingExecutor")
    public  Executor getProcessingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("processingExecutor-");
        executor.initialize();
        return executor;
    }

}
