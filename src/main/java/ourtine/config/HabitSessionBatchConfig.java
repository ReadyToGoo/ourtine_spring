package ourtine.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ourtine.Batch.HabitSessionTasklet;
import ourtine.Batch.HabitTasklet;
import ourtine.converter.DayConverter;
import ourtine.repository.HabitDaysRepository;
import ourtine.repository.HabitRepository;
import ourtine.repository.HabitSessionRepository;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class HabitSessionBatchConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    private HabitDaysRepository habitDaysRepository;
    private HabitSessionRepository habitSessionRepository;
    private DayConverter dayConverter;

    @Bean(name = "HABIT_SESSION_TASKLET")
    public Tasklet habitSessionTasklet(HabitDaysRepository habitDaysRepository,HabitSessionRepository habitSessionRepository,DayConverter dayConverter){
        return new HabitSessionTasklet(habitDaysRepository,habitSessionRepository, dayConverter);
    }

    @Bean(name = "HABIT_SESSION_JOB")
    public Job sessionJob(JobRepository jobRepository, @Qualifier("HABIT_SESSION_STEP")Step step){
        return jobBuilderFactory.get("HABIT_SESSION_JOB").repository(jobRepository)
                .start(step)
                .build();
    }

    @Bean(name = "HABIT_SESSION_STEP")
    public Step habitSessionStep(JobRepository jobRepository){
        return stepBuilderFactory.get("HABIT_SESSION_STEP").repository(jobRepository)
                .tasklet(habitSessionTasklet(habitDaysRepository,habitSessionRepository,dayConverter)) // Tasklet 설정
                .build();
    }

}
