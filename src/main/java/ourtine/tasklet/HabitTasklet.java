package ourtine.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import ourtine.domain.Habit;
import ourtine.domain.enums.Status;
import ourtine.repository.HabitRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
public class HabitTasklet implements Tasklet {
    private final HabitRepository habitRepository;

    @Autowired
    public HabitTasklet(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;

    }

    // 습관 종료 시간 15분 후에 습관을 비활성화 시킨다.
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDate endDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        LocalTime endTime = now.minusMinutes(5);

        List<Habit> habits = habitRepository.queryFindHabitsByEndTime(endTime,endDate);
        for(Habit habit: habits){
            habit.setStatus(Status.INACTIVE);
        }
        return RepeatStatus.FINISHED;
    }
}
