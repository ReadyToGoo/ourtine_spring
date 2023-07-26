package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Category;
import ourtine.domain.Habit;
import ourtine.domain.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitRecommendListResponseDto {
    private Long id;

    private String title;

    private String imageUrl;

    private String category;

    private String hostName;

    public HabitRecommendListResponseDto(Habit habit, Category category, User host){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.category = category.getName();
        this.hostName = host.getNickname();
    }
}
