package ourtine.service;

import org.springframework.data.domain.Slice;
import ourtine.domain.User;
import ourtine.domain.enums.Day;
import ourtine.domain.enums.Sort;
import ourtine.server.web.dto.response.*;
import org.springframework.transaction.annotation.Transactional;

public interface HabitService {

    // 습관 참여 여부
    @Transactional
    public boolean getUserIsHabitFollower(Long habitSessionId, User user);
    // 홈 - 팔로잉하는 습관 목록 (요일 필터링)

    @Transactional
    public Slice<HabitMyFollowingListGetResponseDto> getMyFollowingHabits(User user);

    // 습관 상세 정보조회 (참여 x)
    @Transactional
    public HabitNotFollowingGetResponseDto getNotFollowingHabit(Long habitId, User user);

    // 습관 상세 정보조회 (참여 o)
    @Transactional
    public HabitFollowingGetResponseDto getFollowingHabit(Long habitId, User user);

    // 친구 프로필 - 팔로잉 하는 습관 목록
    @Transactional
    public HabitFriendFollowingListGetResponse getFriendFollowingHabits(User friend, User me);

    // 유저 프로필 - 팔로잉 하는 습관 목록
    @Transactional
    public HabitUserFollowingListGetResponse getUserFollowingHabits(User user);

    // 추천 습관 목록
    @Transactional
    public Slice<HabitRecommendListResponseDto> getRecommendHabits(User user);

    //습관 참여하기
    @Transactional
    public HabitJoinPostResponseDto joinHabit(Long habitId, User user);

    @Transactional
    // 습관 검색
    public Slice<HabitSearchResponseDto> searchHabits(Sort sort, User user, String keyword);

    // 습관 탈퇴하기
    @Transactional
    public HabitFollowerResponseDto quitHabit(Long habitId, User user);
}
