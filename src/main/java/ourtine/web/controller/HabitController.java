package ourtine.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ourtine.aws.s3.UploadService;
import ourtine.domain.Habit;
import ourtine.domain.User;
import ourtine.domain.enums.CategoryList;
import ourtine.web.dto.common.SliceResponseDto;
import ourtine.domain.enums.Sort;
import ourtine.web.dto.request.HabitCreatePostRequestDto;
import ourtine.service.impl.HabitServiceImpl;
import ourtine.web.dto.request.HabitInvitationPostRequestDto;
import ourtine.web.dto.response.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/habits")
public class HabitController {

    private final HabitServiceImpl habitService;
    private final UploadService uploadService;

    // 습관 개설
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(value = "습관 개설 - 습관 개설하기", notes = "습관을 개설한다.")
    public HabitCreatePostResponseDto createHabit(@RequestPart @Valid HabitCreatePostRequestDto habitCreatePostRequestDto, @RequestPart MultipartFile file, User user) throws IOException {
        if (file.isEmpty()){ } // TODO: 에러 처리
        return habitService.createHabit(habitCreatePostRequestDto,file,user);
        // TODO: 응답 형식 추가해야함
    }

    // 홈 - 팔로잉하는 습관 목록
    @GetMapping("/me")
    @ApiOperation(value = "홈 - 습관 참여 리스트", notes = "오늘 내가 진행할 습관들을 조회한다.")
    public SliceResponseDto<HabitMyFollowingListGetResponseDto> getMyFollowingHabits(User user, Pageable pageable){
        return new SliceResponseDto<>(habitService.getMyFollowingHabits(user,pageable));
    }

    // 습관 프로필 조회
    @GetMapping(value = "/{habit_id}")
    @ApiOperation(value = "습관 프로필", notes = "특정 습관 하나를 조회한다.")
    public HabitGetResponseDto getHabit(@PathVariable Long habit_id, User user){
        return habitService.getHabit(habit_id,user);
    }

    // 습관 프로필 사진 업로드
    @PatchMapping(value="/habit/{id}/profile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "습관 프로필 사진 변경",notes="습관의 프로필 사진을 변경한다.")
    public void updateHabitProfileImage(@PathVariable Long id, HttpServletRequest request, @RequestParam(value="image") MultipartFile image) throws IOException {
        // 자신이 생성한 Habit인지 체크해야 함.
        Habit habit = habitService.findById(id);
        habit.updateImage(uploadService.uploadHabitProfile(image));
        habitService.saveOrUpdateHabit(habit);
    }


    // 습관 위클리 로그
    @GetMapping(value = "/{habit_id}/weekly-log")
    @ApiOperation(value = "습관 프로필", notes = "내가 참여하는 특정 습관에 대한 위클리로그를 조회한다.")
    public SliceResponseDto<HabitWeeklyLogResponseDto> getHabitWeeklyLog(@PathVariable Long habit_id,User user){
        return new SliceResponseDto<>(habitService.getHabitWeeklyLog(habit_id,user));
    }

    // 유저 프로필 - 팔로잉 하는 습관 목록
    @GetMapping(value = "/users/{user_id}")
    @ApiOperation(value = "유저 프로필", notes = "특정 유저 프로필을 조회한다.")
    public HabitUserFollowingListGetResponseDto getUserFollowingHabits(@PathVariable Long user_id, User me, Pageable pageable){
        return habitService.getUserFollowingHabits(user_id, me, pageable);
    }

    // 추천 습관 목록
    @GetMapping(value = "/recommend")
    @ApiOperation(value = "참여 - 추천 습관", notes = "유저가 관심있는 카테고리에 대한 습관을 조회한다.")
    public SliceResponseDto<HabitRecommendResponseDto> getRecommendHabits(User user, Pageable pageable){
        return new  SliceResponseDto<>(habitService.getRecommendHabits(user, pageable));
    }

    // 습관 참여하기
    @PostMapping(value = "/{habit_id}")
    @ApiOperation(value = "습관 참여", notes = "습관에 참여 신청을 한다.")
    public HabitFollowerResponseDto joinHabit(@PathVariable @Valid Long habit_id, User user){
        return habitService.joinHabit(habit_id,user);
    }

    // 습관 검색하기
    @GetMapping(value = "/search")
    @ApiOperation(value = "참여 - 검색", notes = "원하는 정렬 필터와 키워드로 습관을 검색한다.")
    public SliceResponseDto<HabitSearchResponseDto> searchHabits(@RequestParam Sort sort_by,  @RequestParam String keyword, User user, Pageable pageable){
        String word = '%'+keyword+'%';
        return new SliceResponseDto<>(habitService.searchHabits(sort_by,user,word,pageable));
    }

    // 카테고리별 검색
    @GetMapping("/discover")
    @ApiOperation(value = "참여 - 카테고리별 습관 추천", notes = "카테고리로 분류된 습관들을 조회한다.")
    public SliceResponseDto<HabitFindByCategoryGetResponseDto> findHabitsByCategory(@RequestParam CategoryList category, User user, Pageable pageable){
        return new SliceResponseDto<>(habitService.findHabitsByCategory(category, user, pageable));
    }

    // 습관 참여 취소하기
    @DeleteMapping(value = "/{habit_id}")
    @ApiOperation(value = "습관 프로필 - 습관 참여 취소", notes = "참여하고 있는 습관을 취소한다.")
    public HabitFollowerResponseDto quitHabit(@PathVariable Long habit_id, User user){
        return habitService.quitHabit(habit_id, user);
    }

    // 습관 삭제
    @DeleteMapping(value = "/{habit_id}/delete")
    @ApiOperation(value = "습관 프로필 - 습관 삭제 ", notes = "습관을 삭제한다.")
    public HabitDeleteResponseDto deleteHabit(@PathVariable Long habit_id, User user){
        return habitService.deleteHabit(habit_id, user);
    }

    @PostMapping("/invite")
    @ApiOperation(value = "습관 개설 - 습관 초대", notes = "유저들에게 습관 초대장을 보낸다.")
    public HabitInvitationPostResponseDto sendInvitation (@RequestBody HabitInvitationPostRequestDto requestDto, User user){
        return habitService.sendInvitation(user,requestDto);
    }

}

