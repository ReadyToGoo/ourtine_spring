package ourtine.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;
import ourtine.domain.User;
import ourtine.domain.enums.Sort;
import ourtine.web.dto.request.HabitCreatePostRequestDto;
import org.springframework.transaction.annotation.Transactional;
import ourtine.web.dto.request.HabitInvitationPostRequestDto;
import ourtine.web.dto.response.*;

import java.io.IOException;

public interface HabitService {
    //습관 개설하기
    @Transactional
    public HabitCreatePostResponseDto createHabit(HabitCreatePostRequestDto habitCreatePostRequestDto, MultipartFile file, User user) throws IOException;

    // 홈 - 팔로잉하는 습관 목록 (요일 필터링)
    // @Transactional
    public Slice<HabitMyFollowingListGetResponseDto> getMyFollowingHabits(User user, Pageable pageable);

    // 습관 프로필 조회
    // @Transactional
    public HabitGetResponseDto getHabit(Long habitId, User user);

    // 유저 프로필 - 팔로잉 하는 습관 목록
    // @Transactional
    public HabitUserFollowingListGetResponseDto getUserFollowingHabits(Long userId, User me, Pageable pageable);

    // 추천 습관 목록
    // @Transactional
    public Slice<HabitRecommendResponseDto> getRecommendHabits(User user, Pageable pageable);

    //습관 참여하기
    @Transactional
    public HabitFollowerResponseDto joinHabit(Long habitId, User user);


    // @Transactional
    // 습관 검색
    public Slice<HabitSearchResponseDto> searchHabits(Sort sort, User user, String keyword, Pageable pageable);

    // 습관 탈퇴하기
    @Transactional
    public HabitFollowerResponseDto quitHabit(Long habitId, User user);

    // 카테고리별 습관 검색
    public Slice<HabitFindByCategoryGetResponseDto> findHabitsByCategory(String categoryName, User user, Pageable pageable);

    @Transactional
    // 습관 삭제
    public HabitDeleteResponseDto deleteHabit(Long habitId, User user);

    // 습관 위클리 로그
    public Slice<HabitWeeklyLogResponseDto> getHabitWeeklyLog(Long habitId, User user);

    // 습관 초대
    public HabitInvitationPostResponseDto sendInvitation(Long habitId, User user, HabitInvitationPostRequestDto requestDto);
}