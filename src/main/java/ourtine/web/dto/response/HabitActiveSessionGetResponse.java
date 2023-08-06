package ourtine.web.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
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
public class HabitActiveSessionGetResponse {
    private Long id;


    private Long hostId;

    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
    private LocalTime endTime;

    private List<HabitSessionFollowersGetResponseDto> followers = new ArrayList<>();


    public HabitActiveSessionGetResponse(Long habitSessionId,Habit habit,List<HabitSessionFollowersGetResponseDto> followers){
        this.id = habit.getId();
        this.hostId = habit.getHost().getId();
        this.title = habit.getTitle();
        this.startTime = habit.getStartTime();
        this.endTime = habit.getEndTime();
        this.followers = followers;
    }

}
