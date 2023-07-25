package ourtine.server.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HabitUserFollowingListGetResponse {
    private List<HabitFollowingInfoDto> habits;

}
