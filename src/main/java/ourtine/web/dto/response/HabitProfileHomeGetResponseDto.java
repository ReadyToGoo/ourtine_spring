package ourtine.web.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import ourtine.domain.Habit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ourtine.domain.enums.HabitFollowerStatus;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class HabitProfileHomeGetResponseDto {
    private Long habitId;

    private String title;

    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
    private LocalTime endTime;
    private HabitFollowerStatus status;
    private int participationRate; // 내 참여율
    private int mvp ;

    public HabitProfileHomeGetResponseDto(Habit habit, int participationRate, int mvp, boolean status){
        this.habitId = habit.getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.startTime = habit.getStartTime();
        this.endTime = habit.getEndTime();
        if (status) this.status = HabitFollowerStatus.ENTERED;
        else this.status = HabitFollowerStatus.NOT_ENTERED;
        this.participationRate = participationRate;
        this.mvp = mvp;
    }

}
