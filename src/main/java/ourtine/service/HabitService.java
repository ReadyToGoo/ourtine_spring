package ourtine.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.multipart.MultipartFile;
import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.domain.enums.CategoryList;
import ourtine.domain.enums.Sort;
import ourtine.web.dto.request.HabitCreatePostRequestDto;
import org.springframework.transaction.annotation.Transactional;
import ourtine.web.dto.response.*;

import java.io.IOException;
import java.util.List;

public interface HabitService {

    public Habit findById(Long id);
    public Habit saveOrUpdateHabit(Habit habit);

    //습관 개설하기
    @Transactional
    public HabitCreatePostResponseDto createHabit(HabitCreatePostRequestDto habitCreatePostRequestDto, MultipartFile file, User user) throws IOException;

    //습관 개설하기
    public HabitCreatePostResponseDto createHabit2(HabitCreatePostRequestDto habitCreatePostRequestDto, User user);
    //습관 프로필 변경
    public HabitUpdateImagePatchResponseDto updateHabitImage(Long habitId, MultipartFile file, User user) throws IOException;
    // 홈 - 팔로잉하는 습관 목록 (요일 필터링)
    // @Transactional
    public HabitHomeGetResponseDto getTodaysMyHabits(User user, Pageable pageable);

    // 습관 프로필 조회
    // @Transactional
    public HabitGetResponseDto getHabit(Long habitId, User user);


    public Slice<HabitUserFollowedGetResponseDto> getMyHabits(User user, Pageable pageable);

    public Long getMyHabitCount(User user, Pageable pageable);
    // 내 프로필 - 위클리 로그 조회
    public List<HabitWeeklyLogGetResponseDto> getMyWeeklyLog(User user);

    // 유저 프로필 - 팔로잉 하는 습관 목록
    // @Transactional
    public HabitUserFollowingListGetResponseDto getUserFollowingHabits(Long userId, User me, Pageable pageable);

    // 유저 프로필 - 참여했던 습관 목록
    public Slice<HabitUserFollowedGetResponseDto> getUserFollowedHabits(Long userId, User me, Pageable pageable);

    // 추천 습관 목록
    // @Transactional
    public Slice<HabitSearchGetResponseDto> getRecommendHabits(User user, Pageable pageable);

    //습관 참여하기
    @Transactional
    public HabitFollowerResponseDto joinHabit(Long habitId, User user);


    // @Transactional
    // 습관 검색
    public Slice<HabitSearchGetResponseDto> searchHabits(Sort sort, User user, String keyword, Pageable pageable);

    // 습관 탈퇴하기
    @Modifying
    @Transactional
    public HabitFollowerResponseDto quitHabit(Long habitId, User user);

    // 카테고리별 습관 검색
    public Slice<HabitSearchGetResponseDto> findHabitsByCategory(CategoryList categoryName, User user, Pageable pageable);

    @Modifying
    @Transactional
    // 습관 삭제
    public HabitDeleteResponseDto deleteHabit(Long habitId, User user);

    // 습관 프로필 - 습관 데일리 로그
    public Slice<HabitDailyLogGetResponseDto> getHabitWeeklyLog(Long habitId, User user);


}