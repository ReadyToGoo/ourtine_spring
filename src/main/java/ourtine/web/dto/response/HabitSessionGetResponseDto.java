package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Habit;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitSessionGetResponseDto {
    private Long sessionId;

    private Long habitId;

    private Long hostId;

    private String title;

    private LocalTime startTime;

    private LocalTime endTime;

    private List<HabitSessionFollowerResponseDto> followers = new ArrayList<>();

    public HabitSessionGetResponseDto(Long sessionId, Habit habit, List<HabitSessionFollowerResponseDto> followers){
        this.sessionId = sessionId;
        this.habitId = habit.getId();
        this.hostId = habit.getHost().getId();
        this.title = habit.getTitle();
        this.startTime = habit.getStartTime();
        this.endTime = habit.getEndTime();
        this.followers = followers;
    }
}
