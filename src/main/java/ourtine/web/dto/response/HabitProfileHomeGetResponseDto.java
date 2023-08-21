package ourtine.web.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import ourtine.domain.Habit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ourtine.domain.enums.Day;
import ourtine.domain.enums.HabitFollowerStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalTime;
import java.util.Date;

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
    @Enumerated(value = EnumType.STRING)
    private Day day;
    private HabitFollowerStatus status;
    private int participationRate; // 내 참여율
    private int mvpCount ;

    public HabitProfileHomeGetResponseDto(Habit habit, int participationRate, int mvpCount, boolean status, Day day){
        this.habitId = habit.getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.startTime = habit.getStartTime();
        this.endTime = habit.getEndTime();
        this.day = day;
        if (status) this.status = HabitFollowerStatus.ENTERED;
        else this.status = HabitFollowerStatus.NOT_ENTERED;
        this.participationRate = participationRate;
        this.mvpCount = mvpCount;
    }

}
