package ourtine.tasklet;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import ourtine.converter.DayConverter;
import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.repository.HabitFollowersRepository;
import ourtine.repository.HabitRepository;
import ourtine.repository.HabitSessionFollowerRepository;
import ourtine.repository.HabitSessionRepository;
import ourtine.util.CalculatorClass;
import software.amazon.ion.Decimal;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class StarRateTasklet implements Tasklet, StepExecutionListener {
    private final HabitSessionRepository habitSessionRepository;
    private final HabitFollowersRepository habitFollowersRepository;
    private final HabitSessionFollowerRepository habitSessionFollowerRepository;
    private CalculatorClass calculator;
    private List<Habit> habits;

    @Autowired
    public StarRateTasklet(HabitSessionRepository habitSessionRepository, HabitFollowersRepository habitFollowersRepository, HabitSessionFollowerRepository habitSessionFollowerRepository) {
        this.habitSessionRepository = habitSessionRepository;
        this.habitFollowersRepository = habitFollowersRepository;
        this.habitSessionFollowerRepository = habitSessionFollowerRepository;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate yesterday = today.minusDays(1);
        // 어제 진행되었던 습관들을 조회한다.
        habits = habitSessionRepository.queryFindSessionsByDate(java.sql.Date.valueOf(yesterday));
    }
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if (!habits.isEmpty()){
            for (Habit habit : habits){
                List<User> followers = habitFollowersRepository.queryFindHabitFollowers(habit.getId());
                habit.updateStarRate(
                       Decimal.valueOf(calculator.habitStarRate(habit.getId(), habitSessionRepository, habitSessionFollowerRepository)));
            }
        }
        return RepeatStatus.FINISHED;
    }
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }


}
