package ourtine.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ourtine.domain.Category;
import ourtine.domain.Habit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.CategoryList;
import ourtine.domain.enums.Day;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitNotFollowingGetResponseDto {
    private Long id;

    private Long hostId;

    private String title;

    private String detail;

    private List<String> hashtags = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    CategoryList category;
    private String imageUrl;

    private int participateRate;

    private double starRate;

    private List<HabitFollowersGetResponseDto> followerList = new ArrayList<>();

    private List<Day> days;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "kk:mm:ss", timezone = "Asia/Seoul")
    private LocalTime endTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;

    private Boolean isRecruiting;

    private Long followerCount;

    private Long followerLimit;

    public HabitNotFollowingGetResponseDto(Habit habit,int participateRate, double starRate, List<String> hashtags, Category category, List<HabitFollowersGetResponseDto> habitFollowersGetResponseDto,
                                           boolean isRecruiting){
        this.id = habit.getId();
        this.hostId = habit.getHost().getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.detail = habit.getDetail();
        this.participateRate = participateRate;
        this.starRate = starRate;
        this.category = category.getName();
        this.startDate = habit.getStartDate();
        this.endDate = habit.getEndDate();
        this.startTime = habit.getStartTime();
        this.endTime = habit.getEndTime();
        this.hashtags = hashtags;
        this.followerList = habitFollowersGetResponseDto;
        this.followerCount = habit.getFollowerLimit() - habit.getFollowerCount();
        this.followerLimit = habit.getFollowerLimit();
        this.isRecruiting = isRecruiting;
    }
}
