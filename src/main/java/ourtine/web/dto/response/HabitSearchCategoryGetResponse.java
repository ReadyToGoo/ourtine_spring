package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Category;
import ourtine.domain.Habit;
import ourtine.domain.enums.CategoryList;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitSearchCategoryGetResponse {
    Long id;

    String title;

    @Enumerated(value = EnumType.STRING)
    CategoryList category;
    String hostName;

    String imageUrl;

    public HabitSearchCategoryGetResponse(Habit habit, Category category){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.category = category.getName();
        this.hostName = habit.getHost().getNickname();
        this.imageUrl = habit.getImageUrl();
    }

}
