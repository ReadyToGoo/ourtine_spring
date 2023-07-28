package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Habit;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitFindByCategoryGetResponseDto {
    Long id;

    String title;

    String category;

    String hostName;

    String imageUrl;

    public HabitFindByCategoryGetResponseDto(Habit habit, String category){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.category = category;
        this.hostName = habit.getHost().getNickname();
        this.imageUrl = habit.getImageUrl();
    }

}
