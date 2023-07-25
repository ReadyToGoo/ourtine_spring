package ourtine.server.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Category;
import ourtine.domain.Habit;
import ourtine.domain.Hashtag;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HabitFollowingGetResponseDto {
    private Long id;

    private String title;

    private String detail;

    private List<String> hashtags = new ArrayList<>();

    private String category;

    private String imageUrl;

/*  private Long participateRate;

    private Double starRate;*/

    private List<HabitFollowersGetResponseDto> followerList = new ArrayList<>();

    public HabitFollowingGetResponseDto(Habit habit, List<String> hashtags, Category category, List<HabitFollowersGetResponseDto> habitFollowersGetResponseDto){
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.imageUrl = habit.getImageUrl();
        this.detail = habit.getDetail();
        this.category = category.getName();
        this.hashtags = hashtags;
        this.followerList = habitFollowersGetResponseDto;
    }
}
