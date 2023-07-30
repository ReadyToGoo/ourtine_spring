package ourtine.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ourtine.Batch.TestTasklet;
import ourtine.repository.HabitRepository;
import ourtine.repository.HabitSessionRepository;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private HabitRepository habitRepository;
    private HabitSessionRepository habitSessionRepository;


    @Bean
    public Job job(){
        return jobBuilderFactory.get("testJob")
                .start(step())  // Step 설정
                .build();
    }

    @Bean
    public Step step(){
        return stepBuilderFactory.get("tutorialStep")
                .tasklet(new TestTasklet()) // Tasklet 설정
                .build();
    }
}
