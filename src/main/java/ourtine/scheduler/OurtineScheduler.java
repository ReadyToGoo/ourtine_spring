package ourtine.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import ourtine.domain.enums.Day;
import ourtine.repository.HabitDaysRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
    @Autowired
    @Qualifier("VOTE_JOB")
    private Job VoteJob;
    @Autowired
    @Qualifier("STARRATE_JOB")
    private Job StarRateJob;

    @Scheduled(cron = "0 * * * * *")
    public void startVoteJob() {
        JobParameters jobParameters = new JobParametersBuilder().addString("voteJob", LocalDateTime.now().toString()).toJobParameters();
        try {
            jobLauncher.run(VoteJob,jobParameters);
        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

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

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void startSessionJob() {
        JobParameters jobParameters = new JobParametersBuilder().addString(
                "sessionJob", LocalDateTime.now().toString()).toJobParameters();
        try {
            jobLauncher.run(HabitSessionJob,jobParameters);

        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void startStarRateJob() {
        JobParameters jobParameters = new JobParametersBuilder().addString(
                "starRateJob", LocalDateTime.now().toString()).toJobParameters();
        try {
            jobLauncher.run(StarRateJob,jobParameters);
        } catch (JobExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

}
