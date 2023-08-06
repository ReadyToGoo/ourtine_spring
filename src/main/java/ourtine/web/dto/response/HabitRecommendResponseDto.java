package ourtine.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Category;
import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.domain.enums.CategoryList;
import ourtine.domain.enums.Day;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class HabitRecommendResponseDto {
    private Long id;

    private Long hostId;

    private String hostName;

    private String hostImageUrl;

    private String title;

    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    CategoryList category;
    private List<Day> days = new ArrayList<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
    private LocalTime endTime;

    public HabitRecommendResponseDto(Habit habit, Category category, List<Day> days){
        this.id = habit.getId();
        this.hostId = habit.getHost().getId();
        this.hostName = habit.getHost().getNickname();
        this.hostImageUrl = habit.getHost().getImageUrl();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.category = category.getName();
        this.startTime = habit.getStartTime();
        this.endTime = habit.getEndTime();
        this.days = days;
    }
}
