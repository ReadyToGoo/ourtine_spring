package ourtine.server.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Category;
import ourtine.domain.Habit;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitSearchResponseDto {
    private Long id;
    private String title;
    private boolean isRecruiting;
    private List<String> hashtags;
    private String category;

    public HabitSearchResponseDto(Habit habit, boolean isRecruiting, List<String> hashtags, Category category){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.isRecruiting  =isRecruiting;
        this.hashtags =hashtags;
        this.category = category.getName();
    }

}
