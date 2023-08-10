package ourtine.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Category;
import ourtine.domain.Habit;
import ourtine.domain.enums.CategoryList;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitSearchResponseDto {
    private Long id;
    private String title;
    private Boolean isRecruiting;
    private List<String> hashtags;
    @Enumerated(value = EnumType.STRING)
    CategoryList category;
    public HabitSearchResponseDto(Habit habit, Boolean isRecruiting, List<String> hashtags, Category category){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.isRecruiting  =isRecruiting;
        this.hashtags =hashtags;
        this.category = category.getName();
    }

}
