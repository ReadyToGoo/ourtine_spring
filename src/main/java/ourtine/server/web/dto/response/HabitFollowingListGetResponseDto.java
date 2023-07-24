package ourtine.server.web.dto.response;

import ourtine.domain.Habit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitFollowingListGetResponseDto {
    private Long id;

    private String title;

    private String imageUrl;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    /*private Long participateRate;*/

    private boolean completeStatus;

    /*private String emoji;*/

    public HabitFollowingListGetResponseDto(Habit habit){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.startDate = habit.getStartDate();
        this.endDate = habit.getEndDate();
        this.startTime = habit.getStartTime();
        this.endTime = habit.getEndTime();
    }

    public void setCompleteStatus(boolean completeStatus) {
        this.completeStatus = completeStatus;
    }
}
