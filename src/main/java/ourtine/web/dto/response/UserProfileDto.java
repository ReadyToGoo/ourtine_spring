package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ourtine.domain.Category;
import ourtine.domain.User;
import ourtine.domain.mapping.UserCategory;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserProfileDto {
    private String nickname;
    private String imageUrl;
    private String goal;
    private List<Category> userCategoryList;
    private BigDecimal participationRate;
    private long habitCount;
    private boolean isFollowing;
    private long followerCount;
    private long followingCount;

    public UserProfileDto(User user, List<Category> userCategoryList, boolean isFollowing, Long followerCount, Long followingCount) {
        this.nickname = user.getNickname();
        this.imageUrl = user.getImageUrl();
        this.goal = user.getGoal();
        this.userCategoryList = userCategoryList;
        this.participationRate = user.getParticipationRate();
        this.habitCount = user.getHabitCount();
        this.isFollowing = isFollowing;
        this.followerCount=followerCount;
        this.followingCount=followingCount;
    }
}
