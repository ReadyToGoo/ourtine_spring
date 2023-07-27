package ourtine.server.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HabitGetResponseDto {
    private boolean isFollowing;
    private  HabitNotFollowingGetResponseDto notFollowing ;
    private HabitFollowingGetResponseDto following;
}
