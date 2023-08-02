package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Category;
import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.domain.enums.Day;

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

    private String category;

    private List<Day> days = new ArrayList<>();

    private LocalTime startTime;

    private LocalTime endTime;

    public HabitRecommendResponseDto(Habit habit, Category category, User host, List<Day> days){
        this.id = habit.getId();
        this.hostId = habit.getHost().getId();
        this.hostName = host.getNickname();
        this.hostImageUrl = host.getImageUrl();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.category = category.getName();
        this.startTime = habit.getStartTime();
        this.endTime = habit.getEndTime();
        this.days = days;
    }
}
