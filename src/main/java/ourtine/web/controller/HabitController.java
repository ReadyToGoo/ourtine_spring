package ourtine.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ourtine.domain.User;
import ourtine.domain.common.SliceResponseDto;
import ourtine.domain.enums.Sort;
import ourtine.repository.UserRepository;
import ourtine.web.dto.request.HabitCreateRequestDto;
import ourtine.service.impl.HabitServiceImpl;
import ourtine.web.dto.response.*;

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

    // 습관 프로필 조회
    @GetMapping(value = "/{habit_id}")
    public HabitGetResponseDto getHabit(@PathVariable Long habit_id, User user){
        return habitService.getHabit(habit_id,user);
    }

    // 유저 프로필 - 팔로잉 하는 습관 목록
    @GetMapping(value = "/users/{user_id}")
    public HabitUserFollowingListGetResponseDto getUserFollowingHabits(@PathVariable Long user_id, User me, Pageable pageable){
        return habitService.getUserFollowingHabits(user_id, me, pageable);
    }

    // 추천 습관 목록
    @GetMapping(value = "/recommend")
    public SliceResponseDto<HabitRecommendListResponseDto> getRecommendHabits(User user, Pageable pageable){
        return new  SliceResponseDto<>(habitService.getRecommendHabits(user, pageable));
    }

    // 습관 참여하기
    @PostMapping(value = "/{habit_id}")
    public HabitJoinPostResponseDto joinHabit(@PathVariable @Valid Long habit_id, User user){
        return habitService.joinHabit(habit_id,user);
    }

    //습관 검색하기
    @GetMapping(value = "/search")
    public SliceResponseDto<HabitSearchResponseDto> searchHabits(@RequestParam Sort sort_by,  @RequestParam String keyword, User user, Pageable pageable){
        return new SliceResponseDto<>(habitService.searchHabits(sort_by,user,keyword,pageable));
    }

    // 카테고리별 검색
    @GetMapping("/discover")
    public SliceResponseDto<HabitFindByCategoryGetResponseDto> findHabitsByCategory(@RequestParam String category, User user, Pageable pageable){
        return new SliceResponseDto<>(habitService.findHabitsByCategory(category, user, pageable));
    }
    // 습관 탈퇴
    @DeleteMapping(value = "/{habit_id}")
    public HabitFollowerResponseDto quitHabit(@PathVariable Long habit_id, User user){
        return habitService.quitHabit(habit_id, user);
    }


}

