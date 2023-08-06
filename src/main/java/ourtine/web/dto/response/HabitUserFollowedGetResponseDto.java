package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ourtine.domain.Category;
import ourtine.domain.Habit;
import ourtine.domain.enums.CategoryList;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class HabitUserFollowedGetResponseDto {
    private Long id;

    private String title;

    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    CategoryList category;
    private List<String> hashtags = new ArrayList<>();

    public HabitUserFollowedGetResponseDto(Habit habit, Category category , List<String> hashtags){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.category = category.getName();
        this.hashtags  = hashtags;
    }
}
