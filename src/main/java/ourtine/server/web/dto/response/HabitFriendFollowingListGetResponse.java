package ourtine.server.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ourtine.domain.Habit;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HabitFriendFollowingListGetResponse {

    private List<HabitFollowingInfoDto> commonHabits = new ArrayList<>();

    private List<HabitFollowingInfoDto> otherHabits = new ArrayList<>();

}
