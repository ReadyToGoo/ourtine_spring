package ourtine.web.dto.response;


import ourtine.domain.Habit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.CompleteStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitMyFollowingListGetResponseDto {
    private Long habitId;

    private Long sessionId;

    private String title;

    private String imageUrl;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    /*private Long participateRate;*/
    
    private CompleteStatus completeStatus;

    /*private String emoji;*/

    public HabitMyFollowingListGetResponseDto(Habit habit, Long sessionId, CompleteStatus completeStatus){
        this.habitId = habit.getId();
        this.sessionId = sessionId;
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.startDate = habit.getStartDate();
        this.endDate = habit.getEndDate();
        this.startTime = habit.getStartTime();
        this.endTime = habit.getEndTime();
        this.completeStatus = completeStatus;
    }

    public void setCompleteStatus(CompleteStatus completeStatus) {
        this.completeStatus = completeStatus;
    }


}
