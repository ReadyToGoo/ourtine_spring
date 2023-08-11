package ourtine.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitGetResponseDto {

    private Boolean isFollowing;
    private HabitNotFollowingGetResponseDto notFollowing ;
    private HabitFollowingGetResponseDto following;

}
