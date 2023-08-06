package ourtine.web.dto.response;

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

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate startDate;

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
