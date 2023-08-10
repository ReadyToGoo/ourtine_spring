package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ourtine.domain.User;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyPageResponseDto {
    private String nickname;
    private String imageUrl;
    private String goal;
    private Integer participationRate;
    private long habitCount;
    private long followerCount;
    private long followingCount;
    private List<HabitWeeklyLogGetResponseDto> weeklyLog;

    public MyPageResponseDto(User user, Long followerCount, Long followingCount, List<HabitWeeklyLogGetResponseDto> habitWeeklyLogGetResponseDtoList) {
        this.nickname = user.getNickname();
        this.imageUrl = user.getImageUrl();
        this.goal = user.getGoal();
        this.habitCount = user.getHabitCount();
        this.followerCount=followerCount;
        this.followingCount=followingCount;
        this.weeklyLog=habitWeeklyLogGetResponseDtoList;
    }

}
