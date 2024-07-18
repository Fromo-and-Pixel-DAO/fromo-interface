package org.cell.froopyland_interface.support;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yozora
 * Description custom thread pool
 **/
@Configuration
@EnableAsync
@Log4j2
public class ThreadPoolSupport {

    /**
     * corePoolSize
     */
    @Value("${thread.pool.corePoolSize:2}")
    private int corePoolSize;

    /**
     * maxPoolSize
     */
    @Value("${thread.pool.maxPoolSize:1024}")
    private int maxPoolSize;

    /**
     * keepAliveSeconds
     */
    @Value("${thread.pool.keepAliveSeconds:3}")
    private int keepAliveSeconds;

    /**
     * queueCapacity
     */
    @Value("${thread.pool.queueCapacity:0}")
    private int queueCapacity;

    /**
     * description: default thread pool
     *
     * @return org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
     * @author yozora
     */
    @Bean(name = "defaultTaskExecutor")
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        log.info("create schedule thread pool. corePoolSize:{} maxPoolSize:{} keepAliveSeconds:{} queueCapacity:{}", corePoolSize, maxPoolSize, keepAliveSeconds, queueCapacity);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("default-task-");
        executor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor exe) -> {
            try {
                // 利用BlockingQueue的特性，任务队列满时等待放入
                if (!exe.getQueue().offer(r, 30, TimeUnit.SECONDS)) {
                    log.warn("Offer failed:{}", exe);
                    throw new RuntimeException("Task offer failed after 30 sec");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
        return executor;
    }

    /**
     * description: log thread pool
     *
     * @return org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
     * @author yozora
     */
    @Bean(name = "nashLogTaskExecutor")
    public ThreadPoolTaskExecutor getLogExecutor() {
        log.info("create log thread pool. corePoolSize:{} maxPoolSize:{} keepAliveSeconds:{} queueCapacity:{}", corePoolSize, maxPoolSize, keepAliveSeconds, queueCapacity);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setKeepAliveSeconds(10);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("log-task-");
        executor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor exe) -> {
            try {
                if (!exe.getQueue().offer(r, 30, TimeUnit.SECONDS)) {
                    log.warn("Offer failed:{}", exe);
                    throw new RuntimeException("Task offer failed after 30 sec");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
        return executor;
    }
}
