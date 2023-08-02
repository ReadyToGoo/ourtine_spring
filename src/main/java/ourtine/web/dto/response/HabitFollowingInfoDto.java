package ourtine.web.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Habit;

@Getter
@NoArgsConstructor
public class HabitFollowingInfoDto {
    private Long id;

    private String title;

    private String imageUrl;

    public HabitFollowingInfoDto(Habit habit){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
    }
}
