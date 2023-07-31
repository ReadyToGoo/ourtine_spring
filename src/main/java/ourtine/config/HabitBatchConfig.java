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
public class HabitBatchConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    private HabitRepository habitRepository;


    @Bean(name = "HABIT_TASKLET")
    public Tasklet habitTasklet(HabitRepository habitRepository ){
        return new HabitTasklet(habitRepository);
    }


    @Bean(name = "HABIT_JOB")
    public Job habitJob(JobRepository jobRepository, @Qualifier("HABIT_STEP")Step step){
        return jobBuilderFactory.get("HABIT_JOB").repository(jobRepository)
                .start(step)
                .build();
    }


    @Bean(name = "HABIT_STEP")
    public Step habitStep(JobRepository jobRepository){
        return stepBuilderFactory.get("HABIT_STEP").repository(jobRepository)
                .tasklet(habitTasklet(habitRepository))
                .build();
    }
}
