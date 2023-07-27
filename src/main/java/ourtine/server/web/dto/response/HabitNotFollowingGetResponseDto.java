package ourtine.server.web.dto.response;

import ourtine.domain.Category;
import ourtine.domain.Habit;
import ourtine.domain.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.enums.Day;

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

    private String category;

    private String imageUrl;

    private Long participateRate;

    private BigDecimal starRate;

    private List<HabitFollowersGetResponseDto> followerList = new ArrayList<>();

    private List<Day> days;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long followerCount;

    private Long followerLimit;

    public HabitNotFollowingGetResponseDto(Habit habit, List<String> hashtags, Category category, List<HabitFollowersGetResponseDto> habitFollowersGetResponseDto){
        this.id = habit.getId();
        this.hostId = habit.getHost().getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.detail = habit.getDetail();
        this.category = category.getName();
        this.startDate = habit.getStartDate();
        this.endDate = habit.getEndDate();
        this.startTime = habit.getStartTime();
        this.endTime = habit.getEndTime();
        this.hashtags = hashtags;
        this.followerList = habitFollowersGetResponseDto;
        this.followerCount = habit.getFollowerCount();
        this.followerLimit = habit.getFollowerLimit();
    }
}
