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
import ourtine.repository.*;
import ourtine.tasklet.HabitSessionTasklet;
import ourtine.tasklet.HabitTasklet;
import ourtine.converter.DayConverter;
import ourtine.tasklet.VoteTasklet;


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

    private final UserMvpRepository userMvpRepository;

    private final HabitSessionFollowerRepository habitSessionFollowerRepository;

    private final HabitFollowersRepository habitFollowersRepository;

    private final UserRepository userRepository;


    @Bean
    @Qualifier("SESSION_TASKLET")
    public Tasklet habitSessionTasklet(HabitDaysRepository habitDaysRepository,HabitSessionRepository habitSessionRepository,DayConverter dayConverter){
        return new HabitSessionTasklet(habitDaysRepository,habitSessionRepository, dayConverter);
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
    @Qualifier("HABIT_TASKLET")
    public Tasklet habitTasklet(HabitRepository habitRepository ){
        return new HabitTasklet(habitRepository);
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


    @Bean
    @Qualifier("VOTE_TASKLET")
    public Tasklet voteTasklet(UserMvpRepository userMvpRepository,HabitFollowersRepository habitFollowersRepository,
                               HabitSessionRepository habitSessionRepository, HabitSessionFollowerRepository habitSessionFollowerRepository,
                               UserRepository userRepository){
        return new VoteTasklet(userMvpRepository,habitFollowersRepository,habitSessionRepository,habitSessionFollowerRepository, userRepository);
    }


    @Bean
    @Qualifier("VOTE_JOB")
    public Job voteJob(@Qualifier("VOTE_STEP")Step step){
        return jobBuilderFactory.get("VOTE_JOB")
                .start(step)
                .build();
    }

    @Bean
    @Qualifier("VOTE_STEP")
    public Step voteStep(){
        return stepBuilderFactory.get("VOTE_STEP")
                .tasklet(voteTasklet(userMvpRepository,habitFollowersRepository,habitSessionRepository,habitSessionFollowerRepository, userRepository)) // Tasklet 설정
                .build();
    }


}
