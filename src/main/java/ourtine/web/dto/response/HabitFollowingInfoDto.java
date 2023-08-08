package ourtine.web.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Habit;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitFollowingInfoDto {
    private Long id;

    private String title;

    private String imageUrl;

    private int participateRate;

    public HabitFollowingInfoDto(Habit habit){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.participateRate = habit.getParticipateRate();
    }
}
