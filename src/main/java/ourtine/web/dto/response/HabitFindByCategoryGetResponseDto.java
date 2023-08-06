package ourtine.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@AllArgsConstructor
public class HabitFindByCategoryGetResponseDto {
    Long id;

    Long hostId;

    String hostName;

    String hostImageUrl;

    String title;

    String category;

    String imageUrl;

    private List<Day> days = new ArrayList<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
    private LocalTime endTime;

    public HabitFindByCategoryGetResponseDto(Habit habit, Category category, List<Day> days){
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
