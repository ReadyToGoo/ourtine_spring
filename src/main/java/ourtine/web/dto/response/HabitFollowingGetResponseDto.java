package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Category;
import ourtine.domain.Habit;
import ourtine.domain.enums.CategoryList;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitFollowingGetResponseDto {
    private Long id;

    private Long hostId;

    private String title;

    private String detail;

    private List<String> hashtags = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    CategoryList category;

    private String imageUrl;

    private int participateRate;  // 내 참여율

    private List<HabitFollowersGetResponseDto> followerList = new ArrayList<>();


    public HabitFollowingGetResponseDto(Habit habit, List<String> hashtags, Category category, int participateRate,
                                        List<HabitFollowersGetResponseDto> habitFollowersGetResponseDto){
        this.id = habit.getId();
        this.hostId = habit.getHost().getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.detail = habit.getDetail();
        this.category = category.getName();
        this.hashtags = hashtags;
        this.participateRate = participateRate;
        this.followerList = habitFollowersGetResponseDto;

    }
}
