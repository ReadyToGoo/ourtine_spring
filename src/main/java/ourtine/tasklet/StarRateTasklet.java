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
import ourtine.domain.enums.Day;
import ourtine.repository.HabitRepository;

import java.time.LocalDate;
import java.time.ZoneId;

public class StarRateTasklet implements Tasklet, StepExecutionListener {
    private final HabitRepository habitRepository;
    private final DayConverter dayConverter;

    @Autowired
    public StarRateTasklet(HabitRepository habitRepository, DayConverter dayConverter) {
        this.habitRepository = habitRepository;
        this.dayConverter = dayConverter;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        Day day  = dayConverter.curDayOfWeek();
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
    }
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        return null;
    }
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }


}
