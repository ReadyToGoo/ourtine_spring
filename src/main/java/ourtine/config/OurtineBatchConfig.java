package ourtine.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ourtine.tasklet.HabitSessionTasklet;
import ourtine.tasklet.HabitTasklet;
import ourtine.converter.DayConverter;
import ourtine.repository.HabitDaysRepository;
import ourtine.repository.HabitRepository;
import ourtine.repository.HabitSessionRepository;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class OurtineBatchConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    private HabitDaysRepository habitDaysRepository;
    private HabitSessionRepository habitSessionRepository;
    private HabitRepository repository;
    private DayConverter dayConverter;

    @Bean
    @Qualifier("SESSION_TASKLET")
    public Tasklet habitSessionTasklet(HabitDaysRepository habitDaysRepository,HabitSessionRepository habitSessionRepository,DayConverter dayConverter){
        return new HabitSessionTasklet(habitDaysRepository,habitSessionRepository, dayConverter);
    }

    @Bean
    @Qualifier("HABIT_TASKLET")
    public Tasklet habitTasklet(HabitRepository habitRepository ){
        return new HabitTasklet(habitRepository);
    }

    @Bean
    @Qualifier("SESSION_JOB")
    public Job sessionJob(@Qualifier("SESSION_STEP")Step step){
        return jobBuilderFactory.get("SESSION_JOB")
                .start(step)
                .build();
    }

    @Bean
    @Qualifier("SESSION_STEP")
    public Step habitSessionStep(){
        return stepBuilderFactory.get("SESSION_STEP")
                .tasklet(habitSessionTasklet(habitDaysRepository,habitSessionRepository,dayConverter)) // Tasklet 설정
                .build();
    }


    @Bean
    @Qualifier("HABIT_JOB")
    public Job habitJob( @Qualifier("HABIT_STEP")Step step){
        return jobBuilderFactory.get("HABIT_JOB")
                .start(step)
                .build();
    }


    @Bean
    @Qualifier("HABIT_STEP")
    public Step habitStep(){
        return stepBuilderFactory.get("HABIT_STEP")
                .tasklet(habitTasklet(repository))
                .build();
    }

}
