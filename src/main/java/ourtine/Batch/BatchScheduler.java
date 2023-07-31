package ourtine.Batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
@EnableScheduling
@RequiredArgsConstructor
@Configuration
public class BatchScheduler {
    private  final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(){
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }
    @Scheduled(cron = "0 * * * * *")
    public void startHabitJob() {
        JobParameters jobParameters = new JobParametersBuilder().addString("habitJob", LocalDateTime.now().toString()).toJobParameters();
        try {
        jobLauncher.run(
                jobRegistry.getJob("habit-job"),
                jobParameters  // job parameter 설정
        );
        } catch (JobExecutionException ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
    }
    }

    @Scheduled(cron = "0 * * * * *")
    public void startHabitSessionJob(){
        JobParameters jobParameters = new JobParametersBuilder().addString("habitSessionJob", LocalDateTime.now().toString()).toJobParameters();
        try {
            jobLauncher.run(
                    jobRegistry.getJob("habit-session-job"),
                    jobParameters  // job parameter 설정
            );
        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
