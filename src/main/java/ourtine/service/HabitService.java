package ourtine.service;

import ourtine.domain.User;
import ourtine.server.web.dto.response.HabitFollowingListGetResponseDto;
import ourtine.server.web.dto.response.HabitGetResponseDto;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

public interface HabitService {
    @Transactional
    public HabitGetResponseDto getHabit(long habitId, User user);

    // 홈참여중인 습관들 조회
    @Transactional
    public Slice<HabitFollowingListGetResponseDto> getFollowingHabits(User user);
}
