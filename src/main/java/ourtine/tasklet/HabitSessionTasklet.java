package ourtine.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ourtine.converter.DayConverter;
import ourtine.domain.Habit;
import ourtine.domain.HabitSession;
import ourtine.domain.enums.Day;
import ourtine.repository.HabitDaysRepository;
import ourtine.repository.HabitSessionRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
public class HabitSessionTasklet implements Tasklet, StepExecutionListener {
    private final HabitDaysRepository habitDaysRepository;
    private final HabitSessionRepository habitSessionRepository;
    private final DayConverter dayConverter;
    private List<Habit> habits;
    private List<Habit> habitsTomorrow;
    @Autowired
    public HabitSessionTasklet(HabitDaysRepository habitDaysRepository, HabitSessionRepository habitSessionRepository, DayConverter dayConverter) {
        this.habitDaysRepository = habitDaysRepository;
        this.habitSessionRepository = habitSessionRepository;
        this.dayConverter = dayConverter;
    }


    @Override
    public void beforeStep(StepExecution stepExecution) {
        Day day  = dayConverter.curDayOfWeek();
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalTime startTime = LocalTime.of(23,59,59);
        
        // 오늘 진행되는 습관들을 조회한다.
        habits = habitDaysRepository.queryFindHabitsByStartTime(startTime,day);

        // 내일의, 시작 시간이 오전 12시 10분까지의 습관을 찾는다.
        habitsTomorrow = habitDaysRepository.queryFindHabitsByStartTime(LocalTime.of(0,10),
                dayConverter.dayOfWeek(today.getDayOfWeek().getValue()+1));
    }

    // 습관 시작 시간 15분 전에 습관 세션을 생성한다.
    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if (!habits.isEmpty()) {
            // 오늘 진행되는 습관들 생성
            for (Habit habit : habits) {
                // 습관 세션 중복 생성을 막음
                if (habitSessionRepository.queryFindTodaySessionByHabitId(habit.getId()).isEmpty()) {
                        HabitSession habitSession = HabitSession.builder().habit(habit).date(java.sql.Date.valueOf(LocalDate.now())).build();
                        habitSessionRepository.save(habitSession);
                }
            }

        }
        if (!habitsTomorrow.isEmpty()) {
            for (Habit habit : habitsTomorrow) {
                // 습관 세션 중복 생성을 막음
                if (habitSessionRepository.findByHabit_Id(habit.getId()).isEmpty()) {
                    HabitSession habitSession = HabitSession.builder().habit(habit).date(java.sql.Date.valueOf(LocalDate.now().plusDays(1))).build();
                    habitSessionRepository.save(habitSession);
                }
            }
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        return ExitStatus.COMPLETED;
    }
}
