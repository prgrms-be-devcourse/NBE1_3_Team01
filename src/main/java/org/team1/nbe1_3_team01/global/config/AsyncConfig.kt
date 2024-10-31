package org.team1.nbe1_3_team01.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

@Configuration
@EnableAsync
class AsyncConfig {
    @Bean
    fun taskExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 5 // 최소 쓰레드 수
        executor.maxPoolSize = 20 // 최대 쓰레드 수
        executor.queueCapacity = 50 // 대기 큐 용량
        executor.initialize()
        executor.setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
        return executor
    }
}
