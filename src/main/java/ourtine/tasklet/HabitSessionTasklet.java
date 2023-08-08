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
    @Autowired
    public HabitSessionTasklet(HabitDaysRepository habitDaysRepository, HabitSessionRepository habitSessionRepository, DayConverter dayConverter) {
        this.habitDaysRepository = habitDaysRepository;
        this.habitSessionRepository = habitSessionRepository;
        this.dayConverter = dayConverter;
    }


    @Override
    public void beforeStep(StepExecution stepExecution) {
        Day day  = dayConverter.curDayOfWeek();
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        LocalTime createTime = now.plusMinutes(15);
        habits = habitDaysRepository.queryFindHabitsByStartTime(createTime,day); // 오늘 요일의, 시작 시간 15분 후의 습관 조회
    }

    // 습관 시작 시간 15분 전에 습관 세션을 생성한다.
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if (habits.size()>0) {
                for (Habit habit : habits) {
                    // 습관 세션 중복 생성을 막음
                    if (habitSessionRepository.queryFindTodaySessionByHabitId(habit.getId()).isEmpty()) {
                        HabitSession habitSession = HabitSession.builder().habit(habit).date(java.sql.Date.valueOf(LocalDate.now())).build();
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
