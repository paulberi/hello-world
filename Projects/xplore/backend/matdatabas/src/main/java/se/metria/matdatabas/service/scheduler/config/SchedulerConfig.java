package se.metria.matdatabas.service.scheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

	private static final String THREAD_PREFIX = "system-";
	private final int POOL_SIZE = 5;

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
		threadPoolTaskScheduler.setThreadNamePrefix(THREAD_PREFIX);return threadPoolTaskScheduler;
	}

}
