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
import ourtine.service.impl.HabitServiceImpl;
import ourtine.web.dto.request.HabitCreatePostRequestDto;
import ourtine.web.dto.response.*;

import javax.validation.Valid;
import java.io.IOException;
@RestController
@RequiredArgsConstructor
public class HabitTestController {

    private final HabitServiceImpl habitService;
    private final UserRepository userRepository;

    // 습관 개설
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public HabitCreatePostResponseDto creatHabit(@RequestPart @Valid HabitCreatePostRequestDto habitCreatePostRequestDto, @RequestPart MultipartFile file) throws IOException {
        User dana = userRepository.findById(1l).get();
        if (file.isEmpty()){} // TODO: 에러 처리
        return habitService.createHabit(habitCreatePostRequestDto,file,dana);
        // TODO: 응답 형식 추가해야함
    }

    // 홈 - 팔로잉하는 습관 목록
    // 습관 프로필 조회
    @GetMapping(value = "/{habit_id}")
    public HabitGetResponseDto getHabit(@PathVariable Long habit_id ){
        User dana = userRepository.findById(2l).get();
        return habitService.getHabit(habit_id, dana);
    }

    // 습관 위클리 로그
    @GetMapping(value = "/{habit_id}/weekly-log")
    public SliceResponseDto<HabitWeeklyLogResponseDto> getHabitWeeklyLog(@PathVariable Long habit_id){
        User dana = userRepository.findById(2l).get();
        return new SliceResponseDto<>(habitService.getHabitWeeklyLog(habit_id, dana));
    }

    // 유저 프로필 - 팔로잉 하는 습관 목록
    @GetMapping(value = "/users/{user_id}")
    public HabitUserFollowingListGetResponseDto getUserFollowingHabits(@PathVariable Long user_id, Pageable pageable){
        User dana = userRepository.findById(2l).get();
        User friend = userRepository.findById(1l).get();
        return habitService.getUserFollowingHabits(friend.getId(), dana, pageable);
    }

    // 추천 습관 목록
    @GetMapping(value = "/recommend")
    public SliceResponseDto<HabitRecommendResponseDto> getRecommendHabits(Pageable pageable){
        User dana = userRepository.findById(2l).get();
        return new  SliceResponseDto<>(habitService.getRecommendHabits(dana, pageable));
    }

    // 습관 참여하기
    @PostMapping(value = "/{habit_id}")
    public HabitFollowerResponseDto joinHabit(@PathVariable @Valid Long habit_id){
        User dana = userRepository.findById(2l).get();
        return habitService.joinHabit(habit_id,dana);
    }

    // 습관 검색하기
    @GetMapping(value = "/search")
    public SliceResponseDto<HabitSearchResponseDto> searchHabits(@RequestParam Sort sort_by,  @RequestParam String keyword, Pageable pageable){
        User dana = userRepository.findById(2l).get();
        return new SliceResponseDto<>(habitService.searchHabits(sort_by,dana,keyword,pageable));
    }

    // 카테고리별 검색
    @GetMapping("/discover")
    public SliceResponseDto<HabitFindByCategoryGetResponseDto> findHabitsByCategory(@RequestParam String category, Pageable pageable){
        User dana = userRepository.findById(2l).get();
        return new SliceResponseDto<>(habitService.findHabitsByCategory(category, dana, pageable));
    }

    // 습관 참여 취소하기
    @DeleteMapping(value = "/{habit_id}")
    public HabitFollowerResponseDto quitHabit(@PathVariable Long habit_id){
        User dana = userRepository.findById(2l).get();
        return habitService.quitHabit(habit_id, dana);
    }

    // 습관 삭제
    @DeleteMapping(value = "/{habit_id}/delete")
    public HabitDeleteResponseDto deleteHabit(@PathVariable Long habit_id){
        User dana = userRepository.findById(2l).get();
        return habitService.deleteHabit(habit_id, dana);
    }

}

