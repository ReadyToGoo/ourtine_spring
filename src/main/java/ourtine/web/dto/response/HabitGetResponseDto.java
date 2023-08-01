package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HabitGetResponseDto {

    private Boolean isFollowing;
    private HabitNotFollowingGetResponseDto notFollowing ;
    private HabitFollowingGetResponseDto following;

}
