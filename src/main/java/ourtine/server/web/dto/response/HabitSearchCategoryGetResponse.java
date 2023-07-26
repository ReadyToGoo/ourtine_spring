package ourtine.server.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Habit;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitSearchCategoryGetResponse {
    Long id;

    String title;

    String category;

    String hostName;

    String imageUrl;

    public HabitSearchCategoryGetResponse(Habit habit, String category){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.category = category;
        this.hostName = habit.getHost().getNickname();
        this.imageUrl = habit.getImageUrl();
    }

}
