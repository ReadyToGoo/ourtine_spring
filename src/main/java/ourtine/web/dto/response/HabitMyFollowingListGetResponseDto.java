package ourtine.web.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import ourtine.domain.Habit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.Day;
import ourtine.domain.enums.HabitFollowerStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitMyFollowingListGetResponseDto {
    private Long habitId;

    private String title;

    private String imageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:MM:ss", timezone = "Asia/Seoul")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:MM:ss", timezone = "Asia/Seoul")
    private LocalTime endTime;

    private LocalDate startDate;

    private LocalDate endDate;

    private Day day;

    private int mvp;

    public HabitMyFollowingListGetResponseDto(Habit habit, int mvp){
        this.habitId = habit.getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.startDate = habit.getStartDate();
        this.endDate = habit.getEndDate();
        this.startTime = habit.getStartTime();
        this.endTime = habit.getEndTime();
        this.mvp = mvp;
    }


}
