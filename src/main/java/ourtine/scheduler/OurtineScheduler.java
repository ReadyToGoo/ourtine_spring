package ourtine.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableScheduling
@RequiredArgsConstructor
@Configuration
public class OurtineScheduler {
    @Autowired
    private SchedulingConfigurer schedulingConfigurer;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    @Qualifier("HABIT_JOB")
    private Job HabitJob;
    @Autowired
    @Qualifier("SESSION_JOB")
    private Job HabitSessionJob;

   /* @Bean
    public Executor taskExecutor(){
        return Executors.newScheduledThreadPool(4);
    }*/

    @Scheduled(cron = "0 * * * * *")
    public void startHabitJob() {
        JobParameters jobParameters = new JobParametersBuilder().addString("habitJob", LocalDateTime.now().toString()).toJobParameters();
        try {
            jobLauncher.run(HabitJob,jobParameters);
        } catch (JobExecutionException ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void startHabitSessionJob() {
        JobParameters jobParameters = new JobParametersBuilder().addString("habitSessionJob", LocalDateTime.now().toString()).toJobParameters();
        try {
            jobLauncher.run(HabitSessionJob,jobParameters);
        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

}
