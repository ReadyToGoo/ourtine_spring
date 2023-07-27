package ourtine.server.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ourtine.domain.User;
import ourtine.domain.common.SliceResponseDto;
import ourtine.domain.enums.Sort;
import ourtine.server.web.dto.request.HabitCreateRequestDto;
import ourtine.server.web.dto.response.*;
import ourtine.service.impl.HabitServiceImpl;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/habits")
public class HabitController {

    private final HabitServiceImpl habitService;

    // 습관 개설
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public HabitCreateResponseDto creatHabit(@RequestPart @Valid HabitCreateRequestDto habitCreateRequestDto, User user, @RequestPart MultipartFile file){
        return habitService.createHabit(habitCreateRequestDto,user);
        // TODO: 응답 형식 추가해야함
    }

    // 홈 - 팔로잉하는 습관 목록
    @GetMapping()
    public SliceResponseDto<HabitMyFollowingListGetResponseDto> getMyFollowingHabits(User user, Pageable pageable){
        return new SliceResponseDto<>(habitService.getMyFollowingHabits(user,pageable));
    }

    // TODO: dto 확정 필요
    // 습관 프로필 조회 (참여 x)
    @GetMapping(value = "/{habit_id}")
    public HabitGetResponseDto getNotFollowingHabit(@PathVariable Long habit_id, User user){
        return habitService.getHabit(habit_id, user);
    }

    // 유저 프로필 - 팔로잉 하는 습관 목록
    @GetMapping(value = "/users/{user_id}")
    public HabitUserFollowingListGetResponse getUserFollowingHabits(@PathVariable Long user_id, User me, Pageable pageable){
        return habitService.getUserFollowingHabits(user_id, me, pageable);
    }

    // 추천 습관 목록
    @GetMapping(value = "/recommend")
    public SliceResponseDto<HabitRecommendListResponseDto> getRecommendHabits(User user, Pageable pageable){
        return new  SliceResponseDto<>(habitService.getRecommendHabits(user, pageable));
    }

    // 습관 참여하기
    @PostMapping(value = "/{habit_id}")
    public HabitJoinPostResponseDto joinHabit(Long habit_id,User user){
        return habitService.joinHabit(habit_id,user);
    }

    // TODO: 정책 확인 필요
    // 습관 알림

    //습관 검색하기
    @GetMapping(value = "/search")
    public SliceResponseDto<HabitSearchResponseDto> searchHabits(@RequestParam Sort sort_by, String keyword, User user, Pageable pageable){
        return new SliceResponseDto<>(habitService.searchHabits(sort_by,user,keyword,pageable));
    }

    // 카테고리별 검색
    @GetMapping
    public SliceResponseDto<HabitFindByCategoryGetResponse> findHabitsByCategory(@RequestParam String category, User user, Pageable pageable){
        return new SliceResponseDto<>(habitService.findHabitsByCategory(category, user, pageable));
    }

    // 습관 탈퇴
    @DeleteMapping(value = "/{habit_id}")
    public HabitFollowerResponseDto quitHabit(@PathVariable Long habit_id, User user){
        return habitService.quitHabit(habit_id, user);
    }

    // 습관 세션 생성
    @PostMapping(value = "/{habit_id}/habit-sessions")
    public void createHabitSession

}

