package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.HabitFollowerStatus;
import ourtine.domain.mapping.HabitSessionFollower;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitSessionFollowerVoteResponseDto {
    private Long id;


    private String videoUrl;

    private HabitFollowerStatus habitFollowerStatus; // 습관 완료 여부

    public HabitSessionFollowerVoteResponseDto(HabitSessionFollower habitSessionFollower){
        this.id = habitSessionFollower.getFollower().getId();
        this.videoUrl = habitSessionFollower.getVideoUrl();
        this.habitFollowerStatus = habitSessionFollower.getHabitFollowerStatus();
    }

}
