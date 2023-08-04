package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ourtine.domain.Habit;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class HabitUserFollowedGetResponseDto {
    private Long id;

    private String title;

    private String imageUrl;

    private String category;

    private List<String> hashtags = new ArrayList<>();

    public HabitUserFollowedGetResponseDto(Habit habit, String category , List<String> hashtags){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.category = category;
        this.hashtags  = hashtags;
    }
}
